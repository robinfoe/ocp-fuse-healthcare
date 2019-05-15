package com.redhat.healthcare.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.soap.name.ServiceInterfaceStrategy;
import org.apache.camel.model.RedeliveryPolicyDefinition;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.springframework.stereotype.Component;

import com.sun.mdm.index.webservice.PersonEJB;


@Component
public class RouteOutbound extends RouteBuilder{
	
	
	@Override
	public void configure() throws Exception {
		
		ServiceInterfaceStrategy strategy = new ServiceInterfaceStrategy(PersonEJB.class,true);
		SoapJaxbDataFormat dataFormatSoap = new SoapJaxbDataFormat("com.sun.mdm.index.webservice",strategy);
		
		RedeliveryPolicyDefinition exceptionPolicy = new RedeliveryPolicyDefinition();
		exceptionPolicy.setMaximumRedeliveries("3");

		errorHandler(deadLetterChannel("{{queue.nextgate.dlq}}"));
		
		
		from("{{queue.nexgate.inbound}}")
			.to("log:incoming")
			.unmarshal(dataFormatSoap)
			.bean("nextGateBean", "triggerMatch")
			.marshal(dataFormatSoap)
			.to("xslt:sanitize.xsl")
			.to("log:from-nextgate");

	}

}
