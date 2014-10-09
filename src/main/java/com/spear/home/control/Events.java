package com.spear.home.control;

import java.awt.image.BufferedImage;
import java.util.Date;

public class Events {

	// for broadcasting on eventbus
	public static class Status {
		private String status;

		public Status(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
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
