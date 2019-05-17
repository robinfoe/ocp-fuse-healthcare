package com.redhat.healthcare.route;

import javax.xml.bind.JAXBContext;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.stereotype.Component;

import com.customer.app.Person;


@Component
public class RoutePerson extends RouteBuilder{
	
	public static final String ROUTE_PERSON_MARSHALL = "RTP_M";
	

	@Override
	public void configure() throws Exception {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
		JaxbDataFormat jaxbDataFormat = new JaxbDataFormat(jaxbContext);
		
		from("direct:proceedRoute").routeId(RoutePerson.ROUTE_PERSON_MARSHALL)
			.marshal(jaxbDataFormat)
			.to("log:content")			
			.to("{{queue.deim.inbound}}")
			.setBody().xpath("/Envelope/Body/matchColResult/resultCode/text()", String.class)
			.to("log:result");
		
	}

}
