package com.employee.authentication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.jackson.map.ObjectMapper;

import com.employee.pojo.Employee;

public class Hmac256Authentication extends AuthenticationType {
	
	//private static Employee employee;
	private byte[] requestBody;
	private byte[] sharedSecret;
	
	@Override
	public void authenticate(HttpHeaders httpHeaders, Employee employee) {
		
		String requestAuthHeader = getAuthHeader(httpHeaders);
		System.out.println("requestAuthHeader: " + requestAuthHeader);
		
		String selfCreatedHeader = employeeRequest(employee);
		System.out.println("selfCreatedHeader: " + selfCreatedHeader);
		
		if (!(selfCreatedHeader.equals(requestAuthHeader))) {
			throw new IllegalArgumentException("HMAC's do not match. Possible Message Tampering");
		}
		
	}


	@Override
	public String employeeRequest(Employee employee) {
		
		employee.getFirstName();
		employee.getLastName();
		employee.getPhoneNumber();
		System.out.println("getRequestEmployee method: " + employee.getFirstName());
		
    	ObjectMapper requestMapper = new ObjectMapper();
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	try {
			requestBody = requestMapper.defaultPrettyPrintingWriter().writeValueAsBytes((employee));
			output.write(requestBody);
			System.out.println("Outputstream print: " + output);
			sharedSecret = "Hello".getBytes("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		byte[] concat = output.toByteArray();
		System.out.println("Concat: " + concat);
		
		byte[] hmacSHA256 = encode(sharedSecret, concat);
		
		String fullHmac = Base64.encodeBase64String(hmacSHA256);
		System.out.println("Hmac From Auth Class: " + fullHmac);
		
		
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
	
}
	


