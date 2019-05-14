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
		
//		JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
//		JaxbDataFormat dataFormatPerson = new JaxbDataFormat(jaxbContext);
		
		ServiceInterfaceStrategy strategy = new ServiceInterfaceStrategy(PersonEJB.class,true);
		SoapJaxbDataFormat dataFormatSoap = new SoapJaxbDataFormat("com.sun.mdm.index.webservice",strategy);
		
		RedeliveryPolicyDefinition exceptionPolicy = new RedeliveryPolicyDefinition();
		exceptionPolicy.setMaximumRedeliveries("3");
//		onException(ConnectionException.class)
//			.setRedeliveryPolicy(exceptionPolicy);

		errorHandler(deadLetterChannel("{{queue.nextgate.dlq}}"));
		
		from("{{queue.nexgate.inbound}}")
			.to("log:incoming")
			.unmarshal(dataFormatSoap);
		// TODO :: start from here - make webservice call to test service .
		
		
		
//			.convertBodyTo(ExecuteMatchUpdate.class)
//			.to("log:after-convert")
//			.marshal(dataFormatSoap)
//			.to("log:after-soap")
//			.to("{{queue.nexgate.inbound}}");

	}

}
