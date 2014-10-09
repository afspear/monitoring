package com.spear.home.ui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.spear.home.control.ControlEngine;
import com.vaadin.server.StreamResource.StreamSource;

public class RealtimeFeed implements Runnable, StreamSource {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ControlEngine.INSTANCE.getBus().post(this);
		}

	}

	@Override
	public InputStream getStream() {
		BufferedImage image = ControlEngine.INSTANCE.getWebcam().getImage();
		try {
			/* Write the image to a buffer. */
			ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
			ImageIO.write(image, "png", imagebuffer);

			/* Return a stream from the buffer. */
			return new ByteArrayInputStream(imagebuffer.toByteArray());
		} catch (IOException e) {
			return null;
		}
	}

}
