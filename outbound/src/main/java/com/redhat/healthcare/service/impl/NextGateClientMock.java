package com.redhat.healthcare.service.impl;

import org.apache.camel.Exchange;

import com.redhat.healthcare.service.NextGateClient;

public class NextGateClientMock implements NextGateClient{

	public void triggerMatch(Exchange exchange) {
		System.err.println("Mock..... ");
		
	}

}
