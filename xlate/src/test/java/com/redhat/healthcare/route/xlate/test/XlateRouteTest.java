package com.redhat.healthcare.route.xlate.test;

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

import com.redhat.healthcare.route.RouteXlate;

@RunWith(CamelSpringBootRunner.class)
@MockEndpoints
@SpringBootTest
public class XlateRouteTest extends CamelTestSupport{
	
	@EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;
	
	@Autowired
    ProducerTemplate producer;
	
	@Autowired
    private CamelContext camelContext;
	
	private URL fileURL;
	
	@Before
    public void setup() throws Exception {
    	this.fileURL = ClassLoader.getSystemResource("sample-simple-person.xml");
    	this.camelContext.getRouteDefinition(RouteXlate.RT_INCOMING)
    		.autoStartup(true)
    		.adviceWith(camelContext, new AdviceWithRouteBuilder() {
				@Override
				public void configure() throws Exception {
					replaceFromWith("direct:start");
					weaveByToUri("log:after-soap").after().to(resultEndpoint);
					weaveByToUri("{{queue.nexgate.inbound}}").remove();
				}
    		});
    	
    }
	
	
    @Test
    public void sample() throws Exception{
    	String xmlData = new String(Files.readAllBytes(Paths.get(this.fileURL.toURI())));
    	
    	System.err.println(xmlData);
    	resultEndpoint.expectedMessageCount(1);
    	producer.sendBody("direct:start",xmlData);
    	resultEndpoint.assertIsSatisfied();
//    	assertMockEndpointsSatisfied

    	System.err.println("Hello this is test ");
    }

}
