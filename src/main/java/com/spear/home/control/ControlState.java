package com.spear.home.control;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;

public enum ControlState {
	
	INSTANCE;
	
	private static String actionResult;
	private static String status;
		
	public static synchronized String getActionResult() {
		return actionResult;
	}
	public static synchronized void setActionResult(String actionResult) {
		ControlState.actionResult = actionResult;
		
	}
	public static synchronized String getStatus() {
		return status;
	}
	public static synchronized void  setStatus(String status) {
		ControlState.status = status;
	}
	

	

}
