package com.employee.exceptions;

public class JsonMappingException extends RuntimeException{
	
	public JsonMappingException (String errorMessage, Throwable err) {
		super(errorMessage,err);
	}
}
