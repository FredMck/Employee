package com.employee.logger;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import com.employee.pojo.Employee;

public class EmployeeLogger {

    static Logger logger = Logger.getLogger("com.employee.logger.file");
    //static Logger secondLogger = Logger.getLogger("com.employee.logger.secondfile");
    
    public void logIncomingEmployee (HttpServletRequest incomingRequest, Employee employee) {
    	
    	
    	logger.info("--------------------INCOMING REQUEST DETAILS-------------------\n");
    	
    	logger.info("Request Header: " + incomingRequest.getHeader("authorization"));
    	logger.info("URL: " + incomingRequest.getRequestURI());
    	logger.info("IP Addr: " + incomingRequest.getRemoteAddr());
    	logger.info("Method: " + incomingRequest.getMethod());
    	
    	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    	String json = null;
    	try {
			json = ow.writeValueAsString(employee);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	logger.info("Payload: " + json);
    	
    	try {
			if (incomingRequest.getInputStream().available() != 0) {
				logger.info("Payload length: " + incomingRequest.getInputStream().available());
			} else {
				System.out.println("InputStream not available");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    	/*logger.info("firstname: " + employee.getFirstName() + "\nsecondname: " + employee.getLastName() + "\nphonenumber: " + employee.getPhoneNumber());
    	logger.info("Employee: " + employee.toString());*/
    	
    	
    	logger.info("-------------------------------------------------------------");
    	
    	/*try {
			logger.info("Payload: " + new String (IOUtils.toString(incomingRequest.getInputStream())));
			
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    }
	
}
