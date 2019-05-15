package com.redhat.healthcare.route;

import javax.xml.bind.JAXBContext;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

import com.customer.app.Person;


@Component
public class RoutePerson extends RouteBuilder{
	

	@Override
	public void configure() throws Exception {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);

		
		from("direct:proceedRoute")
			.marshal(jaxbDataFormat)
			.to("log:content")
			.to("{{queue.deim.inbound}}")
			.setBody().xpath("/Envelope/Body/matchColResult/resultCode/text()", String.class)
//			.setBody().xpath("/ns2:Envelope/ns2:Body/ns3:matchColResult/resultCode/text()", String.class)
			.to("log:result");
		
//			.setBody().xpath("/ns2:Envelope/ns2:Body/ns3:matchColResult/resultCode/text()", String.class);

		
	}

}
