package com.employee.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.ValidationException;
import javax.xml.crypto.dsig.spec.HMACParameterSpec;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.employee.authentication.AuthEnum;
import com.employee.authentication.AuthenticationType;
import com.employee.authentication.BasicAuthentication;
import com.employee.authentication.Hmac256Authentication;
import com.employee.dao.EmployeeDao;
import com.employee.logger.EmployeeLogger;
import com.employee.pojo.Employee;
import com.employee.pojo.EmployeeAndUserResponse;
import com.employee.pojo.EmployeeResponse;
import com.employee.pojo.EmployeeResponseWithId;
import com.employee.pojo.FirstnameDetails;
import com.employee.pojo.GetEmployeeFirstnameById;
import com.employee.pojo.Users;
import com.sun.jersey.api.NotFoundException;



/*Service class represents the web service, This will consume the requests made to the server and pass intformation to DAO*/
@Path("/employees")
public class EmployeeService{

	
	
		private static AuthenticationType basic;
		private static AuthenticationType sha256;
		private static Employee employee;
		@Context private HttpHeaders httpHeaders;
		@Context private HttpServletRequest request;
		//private EmployeeLogger logger;
		static Logger logger = Logger.getLogger(EmployeeLogger.class);
		
		/*public EmployeeService (@Context HttpServletRequest request) {
			this.request = request;
			EmployeeLogger logger = new EmployeeLogger();
			logger.logIncoming(request);
		}*/

		
		private static void Validation (Employee emp) {
			employee = emp;
			
			if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
				throw new InternalError("Missing firstName");
			}
			
			if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
				throw new InternalError("Missing lastName");
			}
			
			if (employee.getPhoneNumber() == null || employee.getPhoneNumber().trim().isEmpty() || !(employee.getPhoneNumber().length() == 11)) {
				throw new InternalError("Missing phoneNumber or invalid phoneNumber");
			}
			
		}
	
		
		
		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/addemployee") // Adding via webservice URL http://localhost:8080/Employee/rest/employees/addemployee
		public Response addEmployee (Employee employee, InputStream requestBody) throws Exception {
			
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
	        StringBuilder out = new StringBuilder();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            out.append(line);
	        }
	        System.out.println("req body: " + out.toString());   //Prints the string content read from input stream
	        reader.close();


			
			
			System.out.println("----Adding new employee via web service----");
			
			EmployeeLogger logger = new EmployeeLogger();
			logger.logIncomingEmployee(request, employee);
			
			/*Setting the authentication type*/
			sha256 = new Hmac256Authentication();
			//sha256.authenticate(httpHeaders, employee);
			
			//EmployeeService service = new EmployeeService();
			//service.Validation(employee);
			Validation(employee);
			EmployeeDao employeeDao = new EmployeeDao();

					
			/*Creating the response to be sent back*/
			ObjectMapper mapper = new ObjectMapper();
			EmployeeResponse response = new EmployeeResponse();
			
			try {
				
				response.setFirstName(employee.getFirstName());
				response.setLastName(employee.getLastName());
				response.setPhoneNumber(employee.getPhoneNumber());
				
				mapper.writeValue(new ByteArrayOutputStream(), response);
			
				
			} catch (IOException e) {
				throw new NotFoundException("Cannot produce output.");
			}
			
			
			employeeDao.createEmployee(employee);
			System.out.println("----EmployeeDAO Created----");
			
			return Response.status(201).entity(response).build();
		}
		

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/getallemployees") // http://localhost:8080/Employee/rest/employees/getallemployees
		public String getAllEmployees () {
			System.out.println("----Getting all employee's from database----");
			
			/*Setting the authentication type*/
			basic = new BasicAuthentication();
			//basic.authenticate(httpHeaders, employee);
			
			
			EmployeeDao employeeDao = new EmployeeDao();
			List<Employee> employeeList = employeeDao.readAllEmployees();
			//employeeList.stream().forEach(emp -> System.out.println("employee: " + emp));
			
			ObjectMapper mapper = new ObjectMapper();
			EmployeeResponse response = new EmployeeResponse();
			String jsonResponse = null;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			List<String> jsonOutput = new ArrayList<>();
			
			for (Employee em : employeeList) {
				try {
					
					response.setFirstName(em.getFirstName());
					response.setLastName(em.getLastName());
					response.setPhoneNumber(em.getPhoneNumber());
					mapper.defaultPrettyPrintingWriter().writeValue(out, response);
					
					jsonResponse = out.toString();
		
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			jsonOutput.add(jsonResponse);
			String output = jsonOutput.toString();
			System.out.println("\n----Got employee's from database----");
			
			return output;
			
		}
		
		
		
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/getcustomemployee/{firstName}") // http://localhost:8080/Employee/rest/employees/getcustomemployee/Fred
		public List<Employee> getCustomEmployee (@PathParam("firstName") String empFirstname) { 
			System.out.println("----Retreiving Information about Specific Employee's");
			
			basic = new BasicAuthentication();
			basic.authenticate(httpHeaders, employee);
			EmployeeDao employeeDao = new EmployeeDao();
			List<Employee> employeeList = employeeDao.readCustomEmployee(empFirstname);
			
			
			System.out.println("Getting Information About Specified Employee's");
			
			return employeeList;
		}

		
		@DELETE
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/removeEmployee/{id}") //http://localhost:8080/Employee/rest/employees/removeEmployee/26
		public String removeEmployee (@PathParam("id") int idPassedByService) {
			System.out.println("----Deleting employee with id: " + idPassedByService + " from database----");
			
			String jsonResponse = "Deleting Employee Number " + idPassedByService;

			basic = new BasicAuthentication();
			basic.authenticate(httpHeaders, employee);
			EmployeeDao employeeDao = new EmployeeDao();
			employeeDao.deleteEmployee(idPassedByService);
			System.out.println("\n----Employee Deleted----");
			
			return jsonResponse;
		}
		
		
		@PUT
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/editEmployee/{id}") //http://localhost:8080/Employee/rest/employees/editEmployee/20
		public EmployeeResponse editEmployee (@PathParam("id") int idPassedByService, Employee employee) {
			System.out.println("----Updating employee from database----");
			System.out.println("Updating Employee with Id: " + idPassedByService);
			
			//EmployeeService service = new EmployeeService();
			//service.Validation(employee);
			Validation(employee);
			
			ObjectMapper mapper = new ObjectMapper();
			EmployeeResponse response = new EmployeeResponse();
			try {
				mapper.writeValue(new ByteArrayOutputStream(), response);
				
				response.setFirstName(employee.getFirstName());
				response.setLastName(employee.getLastName());
				response.setPhoneNumber(employee.getPhoneNumber());
			} catch (IOException e) {
				throw new NotFoundException("Cannot produce output.");
			}
			
			EmployeeDao employeeDao = new EmployeeDao();
			employeeDao.updateEmployee(idPassedByService, employee);
			
			return response;
			
		}
		
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/firstname/{id}")
		public String getFirstnameById (@PathParam("id") Integer id) {
			
			EmployeeDao empDao = new EmployeeDao();
			GetEmployeeFirstnameById getEmployeeFirstnameById = empDao.getFirstnameByIdDao(id);
			
			ObjectMapper mapper = new ObjectMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String jsonResponse = null;
			
			try {
				
				mapper.writeValue(out, getEmployeeFirstnameById);
				jsonResponse = out.toString();
				
			} catch (IOException e) {
				System.err.println("Cannot write to ObjectMapper:\n");
				e.printStackTrace();
			}
			
			return jsonResponse;
		}
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/multipletables/employeeid/{employeeid}/userid/{userid}")
		public String getFirstNameAndUserByIdUsing2DBTables (@PathParam("employeeid") Integer employeeId, @PathParam("userid") String userId) {
			
			
			EmployeeDao dao = new EmployeeDao();
			EmployeeAndUserResponse response = dao.getFirstnameAndUserById(employeeId, userId);
			
			ObjectMapper mapper = new ObjectMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String jsonResponse = null;
			
			try {
				mapper.writeValue(out, response);
				
			} catch (IOException e) {
				System.err.println("Cannot write value");
				e.printStackTrace();
			}
			
			jsonResponse = out.toString();
			
			return jsonResponse;
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/object/employeeid/{employeeid}/userid/{userid}")
		public String getFirstNameAndUserByIdWithObject (@PathParam("employeeid") Integer employeeId, @PathParam("userid") String userId) {
			
			
			EmployeeDao dao = new EmployeeDao();
			FirstnameDetails response = dao.getFirstnameAndUserByIdWithObjectName(employeeId, userId);
			
			ObjectMapper mapper = new ObjectMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String jsonResponse = null;
			
			try {
				mapper.writeValue(out, response);
				
			} catch (IOException e) {
				System.err.println("Cannot write value");
				e.printStackTrace();
			}
			
			jsonResponse = out.toString();
			
			return jsonResponse;
		}
		
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/filtered")
		public String testingQueryParams (@QueryParam("start") int startId) { 
			
			EmployeeDao empDao = new EmployeeDao();
			List<EmployeeResponse> response = empDao.GreaterThanListOfEmployeeById(startId);
			
			ObjectMapper mapper = new ObjectMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String jsonResponse = null;
			
			
				try {
					mapper.writeValue(out, response);
					
				} catch (IOException e) {
					System.err.println("Cannot write to ObjectMapper:\n");
					e.printStackTrace();
				}
				jsonResponse = out.toString();
			
			
			
			return jsonResponse;
		}
		
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/filteredwithid")
		public String testingQueryParamsWithId (@QueryParam("start") int startId) { 
			
			EmployeeDao empDao = new EmployeeDao();
			List<EmployeeResponseWithId> response = empDao.GreaterThanListOfEmployeeByIdWithId(startId);
			
			ObjectMapper mapper = new ObjectMapper();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			String jsonResponse = null;
			
			
				try {
					mapper.writeValue(out, response);
					
				} catch (IOException e) {
					System.err.println("Cannot write to ObjectMapper:\n");
					e.printStackTrace();
				}
				jsonResponse = out.toString();
	
			return jsonResponse;
		}
		
		@POST
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		@Path("/createuser")
		public Response addUser (Users user) {
			
			basic = new BasicAuthentication();
			basic.authenticate(httpHeaders, employee);
			
			EmployeeDao dao = new EmployeeDao();
			dao.createUser(user);
			
			return Response.status(204).build();
		}
		
	
		
/*		private static String getAuthHeader(@Context HttpHeaders httpHeaders) {
			
			String authorization = null;
			try {
				authorization = httpHeaders.getRequestHeader("authorization").get(0);
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.out.println("Please supply credentials");
			}
			
			switch (authorization) {
			case "BasicAuth": 
				authorization.contains("Basic");
				String[] encoded = authorization.split("\\s+");
				authorization = encoded[2];
				break;
			}
			System.out.println("Authorization Header: " + authorization);
			return authorization;
		}*/
		
		private static String getMessageIdHeader(@Context HttpHeaders httpHeaders) {
			String messageId = httpHeaders.getRequestHeader("MessageId").get(0);
			System.out.println("Message ID Header: " + messageId);
			return messageId;
		}
		
		/*Unused*/
		private static String getAuthHeaderServ(HttpServletRequest request) {
			System.out.println("enters getAuthHeaderServ");
			String auth = request.getHeader	("Authorization").toLowerCase();
			System.out.println("Creates Auth: " + auth);
			System.out.println("NEW AUTH HEADER: " + request);
			return auth;
		}
		
		//Get all request headers
		//http://localhost:8080/Employee/rest/employees/headers
		@GET
		@Consumes(MediaType.APPLICATION_JSON)
		@Path("/headers")
		public String getHeaders (@Context HttpHeaders httpHeaders) {
				
		      MultivaluedMap<String, String> rh = httpHeaders.getRequestHeaders();
		      String str = rh.entrySet()
		                     .stream()
		                     .map(e -> e.getKey() + " = " + e.getValue()) //e = entry in the map. for every entry get the key and the value.
		                     .collect(Collectors.joining("\n"));
		      return str;
		  
		}
		
		


		
		
}
