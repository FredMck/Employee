package com.employee.logger;


import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class EmployeeLogger {

    static Logger logger = Logger.getLogger("com.employee.logger.file");
    //static Logger secondLogger = Logger.getLogger("com.employee.logger.secondfile");
    
    public void logIncomingEmployee (HttpServletRequest incomingRequest, String requestBody) {
    	
    	
    	logger.info("--------------------INCOMING REQUEST DETAILS-------------------\n");
    	
    	logger.info("Request Header: " + incomingRequest.getHeader("authorization"));
    	logger.info("URL: " + incomingRequest.getRequestURI());
    	logger.info("IP Addr: " + incomingRequest.getRemoteAddr());
    	logger.info("Method: " + incomingRequest.getMethod());
    	
    	
    	logger.info(requestBody);
    	

    	
    	
    	logger.info("-------------------------------------------------------------");
    	
    }
	
}
