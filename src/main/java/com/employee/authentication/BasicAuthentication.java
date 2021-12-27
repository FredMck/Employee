package com.employee.authentication;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.employee.entity.TblUsers;
import com.employee.logger.EmployeeLogger;
import com.employee.pojo.Employee;
import com.employee.pojo.Users;
import com.userlogin.dao.CredentialsDao;

public class BasicAuthentication extends AuthenticationType {
	static Logger logger = Logger.getLogger(EmployeeLogger.class);
	private static String username;
	private static String password;
	private static String databaseEncodedAuth;
	private List<TblUsers> users;
	

	
	public BasicAuthentication (List<TblUsers> userList) {
		users = userList;
	}
	
	@Override
	public Response authenticate(HttpHeaders httpHeaders, String requestBody) {
		
		/*Getting list of users from database*/
		
		Response response;
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
			response = Response.status(Response.Status.UNAUTHORIZED).build();
			logger.info("Enters unauth");
			throw new IllegalArgumentException("Wrong credentials provided");
			
		} else {
			response = Response.status(Response.Status.OK).build();
		}
		return response;
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
