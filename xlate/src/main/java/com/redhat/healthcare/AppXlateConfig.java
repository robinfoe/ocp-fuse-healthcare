package com.redhat.healthcare;

import javax.jms.ConnectionFactory;

import org.apache.camel.component.jms.JmsComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

@Configuration
public class AppXlateConfig {
	
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
