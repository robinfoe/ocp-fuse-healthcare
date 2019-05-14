package com.redhat.healthcare.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.customer.app.Person;



@Path("/person")
@Service
public interface PersonService {
	
	/*
	 * @GET
	 * 
	 * @Path("/{id}") public Person get(@PathParam("id") int id);
	 * 
	 * @PUT public Person update(Person person);
	 */
	@GET
	public Response get();
	
	@POST
	@Path("/match")
	@Consumes(value = MediaType.APPLICATION_XML)
	public Response match(Person person);
	
	
}
