package com.employee.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import com.employee.pojo.Employee;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTest {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		
/*		
			try {
				ObjectMapper mapper = new ObjectMapper();
				JsonTest test = new JsonTest();
				
				
				InputStream fis = JsonTest.class.getResourceAsStream("D:\\jsonParseTest.txt");
				test = mapper.readValue(fis, JsonTest.class);
				System.out.println("File Input Stream: " + test);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}*/

		
/*	    try {
	        JSONParser parser = new JSONParser();
	        //Use JSONObject for simple JSON and JSONArray for array of JSON.
	        JSONObject data = (JSONObject) parser.parse(
	              new FileReader("D:\\jsonParseTest.txt"));//path to the JSON file.

	        String json = data.toJSONString();
	        
	        System.out.println("JSON FILE READ AS: " + json);
	    } catch (IOException e) {
	        e.printStackTrace();
	        
	    } catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		Employee emp = new Employee();
		ObjectMapper objectMapper = new ObjectMapper();
		
		Employee test = objectMapper.readValue(new File("D:\\jsonParseTest.json"), Employee.class);
/*		//System.out.println("JSON READ AS: " + test);
		System.out.println("firstname: " + test.getEmployeeFirstName());
		System.out.println("lastname: " + test.getEmployeeLastName());
		System.out.println("phonenumber: " + test.getEmplooyeePhoneNumber());*/
		
		
		
		
		
		
		
		
		
		
		
	}
			
}
