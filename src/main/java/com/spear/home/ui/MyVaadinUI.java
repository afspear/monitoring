package com.spear.home.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotActiveException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;

import com.github.sarxos.webcam.Webcam;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.spear.home.control.ControlEngine;
import com.spear.home.control.Events;
import com.spear.home.control.RealtimeFeed;
import com.spear.home.control.Events.Toggle;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.client.BrowserInfo;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import elemental.client.Browser;

@Theme("mytheme")
@SuppressWarnings("serial")
@Push
public class MyVaadinUI extends UI {

	private Label statusLabel;
	private Button arm;
	private Image image;
	EventBus eventBus = ControlEngine.INSTANCE.getBus();

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
	public static class Servlet extends VaadinServlet {

	}

	@Override
	protected void init(VaadinRequest request) {
		
		
		final MainPage mainPage = new MainPage();

		setContent(mainPage);

		mainPage.getHomeMonitoringLabel().addStyleName(
				ValoTheme.LABEL_BOLD + " " + ValoTheme.LABEL_HUGE);

		statusLabel = mainPage.getStatusLabel();
		
		
		
		statusLabel.addStyleName(ValoTheme.LABEL_BOLD + " "
				+ ValoTheme.LABEL_HUGE + " " + ValoTheme.LABEL_COLORED);

		arm = mainPage.getToggleAlarmButton();

		
		

		arm.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				ControlEngine.INSTANCE.toggleMotionDectection();
				

			}
		});

		image = new Image();
		image.setSizeUndefined();
		image.setHeight("100%");

		mainPage.getStreamVerticalLayout().addComponent(image);
		mainPage.getStreamVerticalLayout().setComponentAlignment(image,Alignment.MIDDLE_CENTER);

		eventBus.register(this);
		setArmedVsDisarmed();
		
		Page.getCurrent().addBrowserWindowResizeListener(new BrowserWindowResizeListener() {
			
			@Override
			public void browserWindowResized(BrowserWindowResizeEvent event) {
				
				if (!Page.getCurrent().getWebBrowser().isTouchDevice())
					return;
				// TODO Auto-generated method stub
				
				if (event.getHeight() < event.getWidth())
				{
					mainPage.getHomeMonitoringLabel().setVisible(false);
					statusLabel.setStyleName(ValoTheme.LABEL_TINY + " " + ValoTheme.LABEL_COLORED);
					arm.setStyleName(ValoTheme.BUTTON_SMALL + " " + ValoTheme.BUTTON_DANGER);
				}
				else
				{
					mainPage.getHomeMonitoringLabel().setVisible(true);
					mainPage.getHomeMonitoringLabel().addStyleName(
							ValoTheme.LABEL_BOLD + " " + ValoTheme.LABEL_HUGE);

					
					arm.setStyleName(ValoTheme.BUTTON_HUGE + " " + ValoTheme.BUTTON_DANGER);
					
					
					statusLabel.addStyleName(ValoTheme.LABEL_BOLD + " "
							+ ValoTheme.LABEL_HUGE + " " + ValoTheme.LABEL_COLORED);
				}
			}
		});

	}
	
	private void setArmedVsDisarmed(boolean armed)
	{
		if (armed) {
			statusLabel.setValue("ARMED");
			arm.setCaption("Disarm");
		}
		else{
			statusLabel.setValue("DISARMED");
			arm.setCaption("Arm");
		}
	}

	@Subscribe
	public void messageStatusHander(final Toggle toggle) {
		MyVaadinUI.this.access(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				setArmedVsDisarmed(toggle.isArmed());
			}
		});
	}

	
	@Subscribe
	public void imageHandler(final RealtimeFeed inputStream)
	{
		access(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				StreamResource resource =
				        new StreamResource(inputStream, this.toString());
				if (image.isVisible()) {
					image.setSource(resource);
				}
				
			}
		});
		
	}

	@Override
	public void detach() {
		// TODO Auto-generated method stub
		super.detach();
		eventBus.unregister(this);
	}

}
