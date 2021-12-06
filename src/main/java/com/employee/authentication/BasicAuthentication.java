package com.employee.authentication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.HttpHeaders;

import com.employee.entity.TblUsers;
import com.employee.pojo.Employee;
import com.employee.pojo.Users;
import com.userlogin.dao.CredentialsDao;

public class BasicAuthentication extends AuthenticationType {
	
	private static String username;
	private static String password;
	private static String databaseEncodedAuth;
	private List<TblUsers> users;
	

	
	public BasicAuthentication (List<TblUsers> userList) {
		users = userList;
	}
	
	@Override
	public void authenticate(HttpHeaders httpHeaders, String requestBody) {
		
		/*Getting list of users from database*/
		
		
		String requestBasicHeader = getAuthHeader(httpHeaders);
		
		//List<TblUsers> usersList = credentialsDao.getAllCredentials();
		
		List<String> authList = new ArrayList<>();
		for (TblUsers user : users) {
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
	public String getAuthHeaderFromRequest(HttpHeaders httpHeaders) {
		return httpHeaders.getRequestHeader("authorization").get(0);
	}
	
	@Override
	public String generateAuthHeader(String requestBody) {
		return null;
	}
	
	
/*	@Override
	public String employeeRequest(Employee employee) {
		
		return null;
	}*/
}
