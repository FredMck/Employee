package com.employee.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"firstname", "username"})
public class EmployeeAndUserResponse {

	private String firstname;
	private String username;
	
	
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	@Override
	public String toString() {
		return " Employee firstname= " + firstname + ", User username= " + username;
	}
	
	
}
