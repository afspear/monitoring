package com.spear.home.control;

import org.apache.camel.ProducerTemplate;

import com.github.sarxos.webcam.Webcam;
import com.google.common.eventbus.EventBus;
import com.spear.home.ui.MotionDetector;
import com.spear.home.ui.RealtimeFeed;

public enum ControlEngine {
	INSTANCE;
	
	private EventBus bus;
	private Webcam webcam;
	private MotionDetector motionDetector;
	

	public Webcam getWebcam() {
		return webcam;
	}

	public void init() {
		bus = new EventBus();
		webcam = Webcam.getDefault();
		webcam.open();
		motionDetector = new MotionDetector();
		
		new Thread(new RealtimeFeed()).start();
		
	}

	public void stop() {
		
		webcam.close();
	}
	
	public boolean toggleMotionDectection ()
	{
		boolean detecting = motionDetector.getDetecting().get();
	
		motionDetector.getDetecting().set(!detecting);
		
		return motionDetector.getDetecting().get();
	}
	
	public boolean armed ()
	{
		return motionDetector.getDetecting().get();
	}



	public EventBus getBus() {
		return bus;
	}
	
	public void newEvent(Object event) {
		bus.post(event);
	}
	

}