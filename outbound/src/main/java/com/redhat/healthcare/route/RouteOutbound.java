package com.redhat.healthcare.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.soap.name.ServiceInterfaceStrategy;
import org.apache.camel.model.RedeliveryPolicyDefinition;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.springframework.stereotype.Component;

import com.sun.mdm.index.webservice.PersonEJB;


@Component
public class RouteOutbound extends RouteBuilder{
	
	public static final String RT_INCOMING = "RT_INC";
	public static final String RT_TRANSFORM_UM = "RT_TR_UN";
	public static final String RT_TRANSFORM_MAR = "RT_TR_MAR";
	public static final String RT_BEAN_NEXTGATE = "RT_BEAN_NEXTGATE";
	
	@Override
	public void configure() throws Exception {
		
		ServiceInterfaceStrategy strategy = new ServiceInterfaceStrategy(PersonEJB.class,true);
		SoapJaxbDataFormat dataFormatSoap = new SoapJaxbDataFormat("com.sun.mdm.index.webservice",strategy);
		
		RedeliveryPolicyDefinition exceptionPolicy = new RedeliveryPolicyDefinition();
		exceptionPolicy.setMaximumRedeliveries("3");

		errorHandler(deadLetterChannel("{{queue.nextgate.dlq}}"));
		
		
		from("{{queue.nexgate.inbound}}").routeId(RouteOutbound.RT_INCOMING)
			.to("log:incoming")
			.unmarshal(dataFormatSoap).id(RouteOutbound.RT_TRANSFORM_UM)
			.bean("nextGateBean", "triggerMatch").id(RouteOutbound.RT_BEAN_NEXTGATE)
			.marshal(dataFormatSoap).id(RouteOutbound.RT_TRANSFORM_MAR)
			.to("xslt:sanitize.xsl")
			.to("log:from-nextgate");

	}

}
