package com.employee.authentication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.employee.pojo.Employee;

public class Hmac256Authentication extends AuthenticationType {
	
	private byte[] data;
	private byte[] sharedSecret;
	
	@Override
	public Response authenticate(HttpHeaders httpHeaders, String requestBody) {
		Response response = null;
		String requestAuthHeader = getAuthHeader(httpHeaders);
		System.out.println("requestAuthHeader: " + requestAuthHeader);
		
		String selfCreatedHeader = generateAuthHeader(requestBody);
		System.out.println("selfCreatedHeader: " + selfCreatedHeader);
		
		if (!(selfCreatedHeader.equals(requestAuthHeader))) {
			response = Response.status(Response.Status.UNAUTHORIZED).build();
			throw new IllegalArgumentException("HMAC's do not match. Possible Message Tampering");
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
		
		if(requestBody == null || requestBody.isEmpty()) {
			requestBody = "";
		}
		try {
			data = requestBody.getBytes("UTF-8");
			sharedSecret = "Hello".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 is not supported");
		}
		
		byte[] hmacSHA256 = encode(sharedSecret, data);
		
		String fullHmac = Base64.encodeBase64String(hmacSHA256);
		return fullHmac;
		
	}
	
	public static byte[] encode(byte[] key, byte[] data) {
	    try {

	        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
	        sha256_HMAC.init(secret_key);

	        return sha256_HMAC.doFinal(data);

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (InvalidKeyException e) {
	        e.printStackTrace();
	    }

	    return null;
	}
	
	
	
/*	@Override
	public String employeeRequest(Employee employee) {
		
		return "";
	}*/






}
	


