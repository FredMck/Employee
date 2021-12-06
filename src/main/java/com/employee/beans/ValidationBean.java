package com.employee.beans;

import javax.ejb.Stateless;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.employee.pojo.Employee;

@Stateless
public class ValidationBean {
	
	static Logger logger = Logger.getLogger(ValidationBean.class);
	
	
	public boolean validateIncomingData (Employee employee) {
		
		String firstName = employee.getFirstName();
		String lastName = employee.getLastName();
		String phoneNumber = employee.getPhoneNumber();
		
		Boolean errorCheck = false;
		
		if(checkIfMissing(firstName, "firstName")) {
			errorCheck = true;
		}
		if(checkIfMissing(lastName, "lastName")) {
			errorCheck = true;
		}
		if(checkIfMissing(phoneNumber, "phoneNumber")) {
			errorCheck = true;
		}
		
		return errorCheck;
	}
	
	private boolean checkIfMissing (String incomingData, String name) {
		Boolean fieldMissing = false;
		
		if(StringUtils.isBlank(incomingData)) {
			fieldMissing = true;
			logger.info("The Field  "+ "'" + name + "''" + "Is Missing"); 
		}
		
		return fieldMissing;
	}

}
