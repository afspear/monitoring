package com.spear.home.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.PumpStreamHandler;

import com.google.common.eventbus.EventBus;
import com.sun.istack.logging.Logger;

public enum Camel {
	INSTANCE;

	private CamelContext context;
	
	private final SimpleRegistry registry = new SimpleRegistry();

	public SimpleRegistry getRegistry() {
		return registry;
	}

	public void init() throws Exception {
	
		context = new DefaultCamelContext(registry);
		

		context.addRoutes(new RouteBuilder() {
			public void configure() throws Exception {
				from("direct:toggleMotion")
				.to("http://localhost:8080/0/detection/status")
				.setHeader("status", body(String.class))
				.choice()
					.when(header("status").contains("PAUSE"))
						.setBody(simple("${null}"))
						.to("http://localhost:8080/0/detection/start")
						.setHeader("result", body(String.class))
					.when(header("status").contains("ACTIVE"))
						.setBody(simple("${null}"))
						.to("http://localhost:8080/0/detection/pause")
						.setHeader("result", body(String.class))
				.end()
				.process(new Processor() {
					public void process(Exchange exchange)
							throws Exception {
						String result = exchange.getIn().getHeader(
								"result", String.class);
						result = result.replaceAll("\\n", " ");
						Pattern pattern = Pattern
								.compile(".* Detection (\\w+) .*");
						Matcher matcher = pattern.matcher(result);
						String actionResult = "error";
						if (matcher.find()) {
							actionResult = matcher.group(1);
							ControlState.setActionResult(actionResult);

						}
						
						exchange.getIn().setBody(new Events.Result(actionResult));

					}
				})
				
				.to("guava-eventbus:bus");
			}
		});

		context.addRoutes(new RouteBuilder() {

			@Override
			public void configure() throws Exception {
				from("direct:motionStatus")
						.to("http://localhost:8080/0/detection/status")
						.process(new Processor() {
							public void process(Exchange exchange)
									throws Exception {
								String status = exchange.getIn().getBody(String.class);

								status = status.replaceAll("\n", " ");
								Pattern pattern = Pattern
										.compile(".* Detection status (\\w+) .*");
								Matcher matcher = pattern.matcher(status);
								String detectionStatus = "error";
								if (matcher.find()) {
									detectionStatus = matcher.group(1);
									ControlState.setStatus(detectionStatus);
								}
								exchange.getIn().setBody(new Events.Status(detectionStatus));
							}
						})
										
						.to("guava-eventbus:bus");
			}
		});

		context.start();

	}

	public CamelContext getContext() {
		return context;
	}
	
	public void stop()
	{
		try {
			context.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
