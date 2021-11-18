package com.test.hmac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class Hmac256 {

	
	public static void main(String[] args) throws IOException {
		
		byte[] sharedSecret = "Hello".getBytes("UTF-8");
		byte[] requestBody = "bob".getBytes("UTF-8");
		
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(requestBody);
		
		
		byte[] concat = output.toByteArray();
		
		byte[] hmacSHA256 = encode(sharedSecret, concat);
		
		
		String fullHmac = Base64.encodeBase64String(hmacSHA256);
		System.out.println("Hmac: " + fullHmac);

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
