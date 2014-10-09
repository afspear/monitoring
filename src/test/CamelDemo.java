package com.spear.home.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.camel.ProducerTemplate;

public class CamelDemo {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Camel.INSTANCE.init();
		ProducerTemplate template = Camel.INSTANCE.getContext().createProducerTemplate();
		template.sendBody("direct:toggleMotion", null);
		
		String status = "<html><head><title>Motion 3.2.12</title></head> <body> <a href=/0/detection>&lt;&ndash; back</a><br><br> <b>Thread 0</b> Detection paused </body> </html>";




		Pattern pattern = Pattern.compile(".* Detection (\\w+).*");
		 Matcher matcher = pattern.matcher(status);
		 boolean b = matcher.matches();
		 
		 
		 //System.out.println(matcher.group(1));

	}

}
