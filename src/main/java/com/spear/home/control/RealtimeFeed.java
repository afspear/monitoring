package com.spear.home.control;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;
import com.vaadin.server.StreamResource.StreamSource;

public class RealtimeFeed implements Runnable, StreamSource {
	Webcam webcam;

	public RealtimeFeed(Webcam webcam) {
		this.webcam = webcam;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			ControlEngine.INSTANCE.getBus().post(this);
		}

	}

	@Override
	public InputStream getStream() {
		BufferedImage image = webcam.getImage();
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
