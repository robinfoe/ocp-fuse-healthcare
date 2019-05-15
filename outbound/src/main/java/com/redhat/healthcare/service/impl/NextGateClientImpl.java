package com.redhat.healthcare.service.impl;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceClient;

import org.apache.camel.Exchange;
import org.apache.camel.Message;

import com.redhat.healthcare.service.NextGateClient;
import com.sun.mdm.index.webservice.CallerInfo;
import com.sun.mdm.index.webservice.ExecuteMatchUpdate;
import com.sun.mdm.index.webservice.MatchColResult;
import com.sun.mdm.index.webservice.PersonEJB;
import com.sun.mdm.index.webservice.SystemPerson;


@WebServiceClient(name = "PersonEJBService", targetNamespace = "http://webservice.index.mdm.sun.com/")
public class NextGateClientImpl extends Service implements NextGateClient {
	
	private final static QName SERVICE_NAME = new QName("http://webservice.index.mdm.sun.com/", "PersonEJBService");
	private final static QName PORT_NAME = new QName("http://webservice.index.mdm.sun.com/", "PersonEJB");

	public NextGateClientImpl(URL wsdlUrl) throws Exception{
		super(wsdlUrl, SERVICE_NAME);
	}
	
	private PersonEJB getPort() {
		return super.getPort(PORT_NAME, PersonEJB.class);
	}
	
	public MatchColResult executeMatchUpdate(CallerInfo callerInfo,SystemPerson sysObjBean) {
		try {
			return this.getPort().executeMatchUpdate(callerInfo, sysObjBean);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
	
	public void triggerMatch(Exchange exchange) {
		try {
			Message in = exchange.getIn();
			ExecuteMatchUpdate item = in.getBody(ExecuteMatchUpdate.class);
			MatchColResult result = this.executeMatchUpdate(item.getCallerInfo(), item.getSysObjBean());
			in.setBody(result);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/*
	 * public static void main(String...strings) { try { URL wsdlUrl = new
	 * URL("http://localhost:8480/services/PersonEJBService/PersonEJB?WSDL");
	 * NextGateClientImpl nextGateClient = new NextGateClientImpl(wsdlUrl);
	 * CallerInfo callerInfo = new CallerInfo(); callerInfo.setApplication("APP");
	 * callerInfo.setUser("Robin"); callerInfo.setSystem("xlate");
	 * 
	 * SystemPerson sysObjBean = new SystemPerson(); sysObjBean.setStatus("Hello");
	 * 
	 * PersonBean person = new PersonBean(); person.setFirstName("robin");
	 * sysObjBean.setPerson(person); nextGateClient.executeMatchUpdate(callerInfo,
	 * sysObjBean);
	 * 
	 * }catch(Exception e) { e.printStackTrace(); } }
	 */
	
	

}
