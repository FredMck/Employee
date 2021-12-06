package com.employee.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class Users {

	private String user_id;
	private String username;
	private String password;
	
	
	public Users (String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Users () {
		
	}
	

	@JsonIgnore
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "user_id=" + user_id + ", username=" + username + ", password=" + password;
	}
}
