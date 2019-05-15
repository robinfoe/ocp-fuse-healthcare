package com.redhat.healthcare.service;

import org.apache.camel.Exchange;

public interface  NextGateClient {

	public void triggerMatch(Exchange exchange);
	
}
