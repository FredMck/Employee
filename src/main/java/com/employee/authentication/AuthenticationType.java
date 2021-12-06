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
		
		//Can check for other auth type headers here if needed
			
		return authorization;
	}
	
	
	
	public abstract void authenticate (HttpHeaders httpHeaders, String requestBody);
	//public abstract void authenticateBasic (HttpHeaders httpHeaders);	
	
	
	//public abstract String employeeRequest (Employee employee);
	public abstract String generateAuthHeader (String requestBody);
	public abstract String getAuthHeaderFromRequest(HttpHeaders httpHeaders);
}
