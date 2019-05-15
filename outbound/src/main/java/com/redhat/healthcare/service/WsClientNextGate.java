package com.redhat.healthcare.service;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.springframework.stereotype.Component;

import com.sun.mdm.index.webservice.CallerInfo;
import com.sun.mdm.index.webservice.ExecuteMatchUpdate;
import com.sun.mdm.index.webservice.MatchColResult;
import com.sun.mdm.index.webservice.PersonEJB;
import com.sun.mdm.index.webservice.SystemPerson;

@Component
public class WsClientNextGate {
	
	private static final QName SERVICE_NAME  = new QName("http://server.hw.demo/", "PersonEJBService");
	private static final QName PORT_NAME = new QName("http://server.hw.demo/", "PersonEJBPort");
	
	private PersonEJB client = null;
	
	public WsClientNextGate() {this.init();}
	
	private void init() {
		try {
			
			Service service = Service.create(SERVICE_NAME);
	        String endpointAddress = "http://localhost:8480/services/PersonEJBService/PersonEJB";
	        service.addPort(PORT_NAME, SOAPBinding.SOAP11HTTP_BINDING, endpointAddress);

	        client = service.getPort(PersonEJB.class);
		}catch(Exception e) {e.printStackTrace();}
	}
	
	public MatchColResult executeMatchUpdate(CallerInfo callerInfo,SystemPerson sysObjBean) {
		try {
			System.err.println("Triggering WS CALL ....");
			return this.client.executeMatchUpdate(callerInfo, sysObjBean);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
	
	public void triggerMatch(Exchange exchange) {
		try {
//			System.err.println(exchange.getIn().getBody());
			Message in = exchange.getIn();
			ExecuteMatchUpdate item = in.getBody(ExecuteMatchUpdate.class);
			if(item!=null) {
				MatchColResult result = this.executeMatchUpdate(item.getCallerInfo(), item.getSysObjBean());
			}else {
				System.err.println("Item is empth ");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	

}
