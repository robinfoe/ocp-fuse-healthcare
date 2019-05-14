package com.redhat.healthcare;

import java.util.Arrays;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsComponent;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

import com.redhat.healthcare.service.PersonService;

@Configuration
public class AppInboundConfig {
	
	@Autowired
    private Bus bus;
	
	@Autowired
	private PersonService personService;
	
	/** JOIN CXF as part of the bus  ********************************/
	@Bean
    public Server rsServer() {
        // setup CXF-RS
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setServiceBeans(Arrays.<Object>asList(personService));
        endpoint.setAddress("/");
        endpoint.setFeatures(Arrays.asList(new Swagger2Feature()));
        return endpoint.create();
    }
	
	/** JMS CONFIGURATION ************************************/
	@Bean
    public JmsTransactionManager jmsTransactionManager(final ConnectionFactory connectionFactory) {
        JmsTransactionManager jmsTransactionManager = new JmsTransactionManager();
        jmsTransactionManager.setConnectionFactory(connectionFactory);
        return jmsTransactionManager;
    }

    @Bean
    public JmsComponent jmsComponent(final ConnectionFactory connectionFactory, final JmsTransactionManager jmsTransactionManager) {
        JmsComponent jmsComponent = JmsComponent.jmsComponentTransacted(connectionFactory, jmsTransactionManager);
        return jmsComponent;
    }
}
