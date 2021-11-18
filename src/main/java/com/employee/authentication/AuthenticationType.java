package com.employee.authentication;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import com.employee.pojo.Employee;

public abstract class AuthenticationType {
	
	//@Context HttpHeaders httpHeaders;
	
	protected String getAuthHeader(HttpHeaders httpHeaders) {
		String authorization = null;
		try {
			authorization = httpHeaders.getRequestHeader("authorization").get(0);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.out.println("Please supply credentials");
		}
		
		if (authorization.contains("Basic")) {
			String[] encoded = authorization.split("\\s+");
			authorization = encoded[1];
		}
		
		//More if statements for other types of authentication (HMAC etc)
		/*if () {
			
		}*/
			
		return authorization;
	}
	
	
	
	public abstract void authenticate (HttpHeaders httpHeaders, Employee employee);
		
	
	
	public abstract String employeeRequest (Employee employee);
}
