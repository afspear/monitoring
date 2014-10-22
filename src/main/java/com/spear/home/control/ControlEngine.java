package com.spear.home.control;

import java.util.List;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

import org.apache.camel.ProducerTemplate;

import com.github.sarxos.webcam.Webcam;
import com.google.common.eventbus.EventBus;

public enum ControlEngine {
	INSTANCE;
	
	private EventBus bus;
	private List<Webcam> webcams;
	private WebcamManager webcamManager;
	private List<MotionDetector> motionDetectors;
	private boolean detecting;


	private ControlEngine() {
		bus = new EventBus();
		webcamManager = WebcamManager.INSTANCE;
		detecting = false;
		for (Webcam webcam : webcamManager.geWebcams()) {
			new Thread(new RealtimeFeed(webcam)).start();
		}
		
	
	}

	public void close() {
		
		webcamManager.close();
	}
	
	public void toggleMotionDectection ()
	{
		detecting = !detecting;
		
		for (MotionDetector motionDetector : motionDetectors) {
			motionDetector.getDetecting().set(detecting);
		}
		
		bus.post(new Events.Toggle(detecting) );	
	}
	
	public boolean isArmed ()
	{
		return detecting;
	}
	
	public void newEvent(Object event) {
		bus.post(event);
	}

	public EventBus getBus() {
		// TODO Auto-generated method stub
		return bus;
	}
	

}
