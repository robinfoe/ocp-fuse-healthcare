package com.redhat.healthcare.route;

import javax.xml.bind.JAXBContext;

import org.apache.camel.TypeConversionException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.apache.camel.dataformat.soap.name.ServiceInterfaceStrategy;
import org.apache.camel.model.RedeliveryPolicyDefinition;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.springframework.stereotype.Component;

import com.customer.app.Person;
import com.sun.mdm.index.webservice.ExecuteMatchUpdate;
import com.sun.mdm.index.webservice.PersonEJB;

@Component
public class RouteXlate extends RouteBuilder{
	
	public static final String RT_INCOMING = "RT_INC";
	public static final String RT_OUTGOING = "RT_OUT";
	
	@Override
	public void configure() throws Exception {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
		JaxbDataFormat dataFormatPerson = new JaxbDataFormat(jaxbContext);
		
		ServiceInterfaceStrategy strategy = new ServiceInterfaceStrategy(PersonEJB.class,true);
		SoapJaxbDataFormat dataFormatSoap = new SoapJaxbDataFormat("com.sun.mdm.index.webservice",strategy);

		RedeliveryPolicyDefinition exceptionPolicy = new RedeliveryPolicyDefinition();
		exceptionPolicy.setMaximumRedeliveries("3");
		onException(TypeConversionException.class)
			.setRedeliveryPolicy(exceptionPolicy);

		errorHandler(deadLetterChannel("jms:q.empi.transform.dlq"));
		
		from("{{queue.deim.inbound}}").routeId(RouteXlate.RT_INCOMING)
			.to("log:incoming")
			.unmarshal(dataFormatPerson)
			.convertBodyTo(ExecuteMatchUpdate.class)
			.to("log:after-convert")
			.marshal(dataFormatSoap)
			.to("log:after-soap")
			.to("{{queue.nexgate.inbound}}");

	}

	

}
