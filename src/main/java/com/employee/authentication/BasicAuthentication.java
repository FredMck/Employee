package com.employee.authentication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.employee.pojo.Employee;
import com.employee.pojo.Users;
import com.userlogin.dao.CredentialsDao;

public class BasicAuthentication extends AuthenticationType {
	
	private String username;
	private String password;
	private String databaseEncodedAuth;
	
	@Override
	public void authenticate(HttpHeaders httpHeaders, Employee employee) {
		/*Getting list of users from database*/
		CredentialsDao credentials = new CredentialsDao();
		List<Users> usersList = credentials.getAllCredentials();
		
		String requestBasicHeader = getAuthHeader(httpHeaders);
		List<String> authList = new ArrayList<>();
		for (Users user : usersList) {
			username = user.getUsername();
			password = user.getPassword();
			String concat = username+":"+password;
			
			byte[] basicConcat = null;
			try {
				basicConcat = concat.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			databaseEncodedAuth = Base64.getEncoder().encodeToString(basicConcat);
			authList.add(databaseEncodedAuth);
			
		}
		
		if (!(authList.contains(requestBasicHeader))) {
			throw new IllegalArgumentException("Wrong credentials provided");
		}		
	}

	@Override
	public String employeeRequest(Employee employee) {
		
		return null;
	}
}
