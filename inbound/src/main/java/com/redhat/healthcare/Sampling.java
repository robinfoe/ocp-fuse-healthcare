package com.redhat.healthcare;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.customer.app.Code;
import com.customer.app.Person;
import com.customer.app.PersonName;
import com.redhat.healthcare.constant.ConstantGender;

public class Sampling {
	
	public void init()throws Exception{

		PersonName personName = new PersonName();
		personName.setGiven("Robin");
		personName.setFamily("Foe");
		Code genderCode = new Code();
		genderCode.setCode(ConstantGender.MALE);
		genderCode.setDisplaytext("Male");
		
		
		
		
		Person person = new Person();
		person.setAge(20);
		person.setMothername("tomcat");
		person.setBirthname("Apache");
		person.setLegalname(personName);
		person.setFathername("Red Hat");
		person.setGender(genderCode);
		
		
		
		JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		StringWriter sw = new StringWriter();
		marshaller.marshal(person, sw);
		System.err.println(sw.toString());
		
		
		
//	    personBean.setFirstName(person.getLegalname().getGiven());
//	    personBean.setFatherName(person.getFathername());
//	    personBean.setGender(person.getGender().getCode());
		 
		
	}
	
	public static void main(String...strings) {
		try {
			
			(new Sampling()).init();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
