package com.redhat.healthcare.unittest;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.customer.app.Person;
import com.redhat.healthcare.route.RoutePerson;

@RunWith(CamelSpringBootRunner.class)
@MockEndpoints
@SpringBootTest
public class InboundRouteTest extends CamelTestSupport{
	
	@EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;
	
	
	@EndpointInject(uri = "direct:proceedRoute")
    private ProducerTemplate producer;
	
	@Autowired
    private CamelContext camelContext;
	
//	private URL fileURL;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        return camelContext;
    }
    
    @Before
    public void setup() throws Exception {
//    	this.fileURL = ClassLoader.getSystemResource("person-sample.xml");
    	
    	
    	this.camelContext.getRouteDefinition(RoutePerson.ROUTE_PERSON_MARSHALL)
    		.autoStartup(true)
    		.adviceWith(camelContext, new AdviceWithRouteBuilder() {
				@Override
				public void configure() throws Exception {
//					replaceFromWith(endpoint);
					weaveByToUri("log:content").after().to(resultEndpoint);
					weaveByToUri("{{queue.deim.inbound}}").remove();
//					interceptSendToEndpoint("log*")
//					.skipSendToOriginalEndpoint()
//					.to(resultEndpoint);
					
				}
    			
    		});
    	
    }
    
    
    @Test
    public void sample() throws Exception{
//    	String xmlData = new String(Files.readAllBytes(Paths.get(this.fileURL.toURI())));
    	resultEndpoint.expectedMessageCount(1);
    	producer.sendBody(this.getRequestBody());
    	resultEndpoint.assertIsSatisfied();
//    	assertMockEndpointsSatisfied

    	System.err.println("Hello this is test ");
    }
    
    
    
    private Person getRequestBody() {
    	Person person = new Person();
    	person.setFathername("Hello");
    	person.setAge(25);
    	person.setAgegroup("adult");
    	
    	
    	return person;
    	
    }

}
