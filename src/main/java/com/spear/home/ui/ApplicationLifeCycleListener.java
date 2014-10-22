package com.spear.home.ui;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.spear.home.control.ControlEngine;

@WebListener
public class ApplicationLifeCycleListener implements ServletContextListener {
	
	ControlEngine controlEngine;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub

		controlEngine = ControlEngine.INSTANCE;

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		controlEngine.close();

	}

}
