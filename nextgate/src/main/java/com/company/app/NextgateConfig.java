package com.company.app;


import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.app.test.PersonEJBImpl;

@Configuration
public class NextgateConfig {
	
	@Autowired
    private Bus bus;
	
	/** JOIN CXF as part of the bus  ********************************/
	@Bean
    public Endpoint getEndpoint() {
        // setup CXF-RS
		
		EndpointImpl endpoint = new EndpointImpl(bus, new PersonEJBImpl());
		endpoint.publish("/PersonEJBService");
		return endpoint;
    }

}
