package com.spear.home.ui;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.spear.home.control.ControlEngine;

@WebListener
public class ApplicationLifeCycleListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub

		ControlEngine.INSTANCE.init();

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ControlEngine.INSTANCE.stop();

	}

}
