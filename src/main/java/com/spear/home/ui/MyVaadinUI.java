package com.spear.home.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.annotation.WebServlet;

import com.github.sarxos.webcam.Webcam;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.spear.home.control.ControlEngine;
import com.spear.home.control.ControlState;
import com.spear.home.control.Events;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.client.BrowserInfo;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
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
		// main layout

		final MainPage mainPage = new MainPage();

		setContent(mainPage);

		mainPage.getHomeMonitoringLabel().addStyleName(
				ValoTheme.LABEL_BOLD + " " + ValoTheme.LABEL_HUGE);

		statusLabel = mainPage.getStatusLabel();

		statusLabel.setValue(ControlState.getStatus());
		statusLabel.addStyleName(ValoTheme.LABEL_BOLD + " "
				+ ValoTheme.LABEL_HUGE + " " + ValoTheme.LABEL_COLORED);

		arm = mainPage.getToggleAlarmButton();

		arm.setCaption("Arm/Disarm");
		arm.addStyleName(ValoTheme.BUTTON_HUGE + " " + ValoTheme.BUTTON_DANGER);

		arm.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				

			}
		});

		image = new Image();
		image.setSizeUndefined();
		image.setHeight("100%");

		mainPage.getStreamVerticalLayout().addComponent(image);
		mainPage.getStreamVerticalLayout().setComponentAlignment(image,Alignment.MIDDLE_CENTER);

		eventBus.register(this);

	}

	@Subscribe
	public void messageStatusHander(final Events.Status status) {
		System.out.println("Message received from the Camel: "
				+ status.getStatus());
		MyVaadinUI.this.access(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				statusLabel.setValue(status.getStatus());
			}
		});
	}

	@Subscribe
	public void messageResultHander(final Events.Result result) {
		System.out.println("Message received from the Camel: "
				+ result.getResult());
		access(new Runnable() {

			@Override
			public void run() {
				Notification notification = new Notification("Result",
						ControlState.getActionResult(), Type.TRAY_NOTIFICATION);
				notification.setDelayMsec(5000);
				notification.show(Page.getCurrent());
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
