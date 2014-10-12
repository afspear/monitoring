package com.spear.home.control;

import java.awt.image.BufferedImage;
import java.util.Date;

public class Events {

	// for broadcasting on eventbus
	public static class Toggle {
		private boolean armed;

		public Toggle(boolean armed) {
			this.armed = armed;
		}

		public boolean isArmed() {
			return armed;
		}
	}

	public static class Result {
		private String result;

		public Result(String result) {
			this.result = result;
		}

		public String getResult() {
			return result;
		}
	}
	
	public static class Motion {
		private BufferedImage image;
		private Date dateTimeStamp;
		
		public Motion(BufferedImage image, Date dateTimeStamp) {
			super();
			this.image = image;
			this.dateTimeStamp = dateTimeStamp;
		}

		public BufferedImage getImage() {
			return image;
		}

		public Date getDateTimeStamp() {
			return dateTimeStamp;
		}
		
		
		
	}

}
