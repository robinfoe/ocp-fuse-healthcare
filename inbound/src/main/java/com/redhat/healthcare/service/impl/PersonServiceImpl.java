package com.redhat.healthcare.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.springframework.stereotype.Component;

import com.customer.app.Person;
import com.customer.app.response.ESBResponse;
import com.redhat.healthcare.constant.HealthcareAction;
import com.redhat.healthcare.enumeration.ResultStatusEnum;
import com.redhat.healthcare.service.PersonService;



@Component
public class PersonServiceImpl implements PersonService{
	
	
	@Produce(uri = "direct:proceedRoute")
	private ProducerTemplate template;

	
	
	public Response get() {
		System.err.println("Hello");
		
		ResponseBuilder builder = new ResponseBuilderImpl();
		builder.status(Response.Status.OK);
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("METHOD", "match");
	    template.sendBody("direct:proceedRoute", new Person());
	    
		return builder.build();
	}
	
	
	public Response match(Person person) {
		System.err.println(person.getBirthname());
		
		ResponseBuilder builder = new ResponseBuilderImpl();
		builder.status(Response.Status.OK);
		
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put(HealthcareAction.OPS_NAME, HealthcareAction.OPS_ACT_MATCH);

		try {
			
			String camelResponse = template.requestBodyAndHeaders(template.getDefaultEndpoint(),person, headers, String.class);
			ResultStatusEnum resultStatus = ResultStatusEnum.findByCode(camelResponse);
			
			ESBResponse esbResponse = new ESBResponse();
			esbResponse.setBusinessKey(UUID.randomUUID().toString());
			esbResponse.setPublished(true);
			esbResponse.setComment(resultStatus.getDescription());
			
			builder.status(Response.Status.OK);
		    builder.entity(esbResponse);
			
		}catch(Exception e) {
			builder.status(Response.Status.INTERNAL_SERVER_ERROR);
			builder.entity(e.getMessage());
			e.printStackTrace();
		}
		
		
		return builder.build();
	}
	
	
}
