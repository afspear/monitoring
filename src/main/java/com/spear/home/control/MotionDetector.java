package com.spear.home.control;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamMotionDetector;
import com.github.sarxos.webcam.WebcamMotionEvent;
import com.github.sarxos.webcam.WebcamMotionListener;

public class MotionDetector implements WebcamMotionListener {
	
	private final Webcam webcam;
	private final AtomicBoolean detecting = new AtomicBoolean();
	
	public MotionDetector(Webcam webcam) {
		this.webcam = webcam;
		WebcamMotionDetector detector = new WebcamMotionDetector(webcam);
		detector.setInterval(500); // one check per 500 ms
		detector.addMotionListener(this);
		detector.start();
	}

	public AtomicBoolean getDetecting() {
		// TODO Auto-generated method stub
		return detecting;
	}

	@Override
	public void motionDetected(WebcamMotionEvent arg0) {
		if (!detecting.get()) {
			    return;
			}
			
			Date date = new Date();
			String formattedDate = new SimpleDateFormat(
			        "yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
			File f = new File(formattedDate + ".png");
			try {
			    BufferedImage image = webcam.getImage();
			    ImageIO.write(image, "PNG", f);
			    System.out.println("Motion detected " + f.getAbsolutePath());
			    ControlEngine.INSTANCE.newEvent(new Events.Motion(image, date));
			
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			    // error message
			}

		
	}

}
