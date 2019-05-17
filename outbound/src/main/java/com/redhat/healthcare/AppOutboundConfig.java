package com.redhat.healthcare;

import java.net.URL;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

import com.redhat.healthcare.service.NextGateClient;
import com.redhat.healthcare.service.impl.NextGateClientImpl;
import com.redhat.healthcare.service.impl.NextGateClientMock;

@Configuration
public class AppOutboundConfig {
	
	@Value("${nextgate.url}")
	private String nextgateUrl;
	
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
    
    @Bean("nextGateBean")
    public NextGateClient createClient() throws Exception{
    	
    	NextGateClient client = null;
    	try {
    		URL url = new URL(this.nextgateUrl);
    		client = new NextGateClientImpl(url); 
    	}catch(Exception e)
    	{/** IGNORED **/}    	
    	
    	
    	return (client == null) ? new NextGateClientMock() :  client;
    }

}
