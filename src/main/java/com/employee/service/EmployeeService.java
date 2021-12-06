package com.employee.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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



import org.apache.log4j.Logger;

import com.employee.authentication.AuthEnum;
import com.employee.authentication.AuthenticationType;
import com.employee.authentication.BasicAuthentication;
import com.employee.authentication.Hmac256Authentication;
import com.employee.beans.ValidationBean;
import com.employee.dao.beans.RequestLoggingBean;
import com.employee.entity.TblEmployee;
import com.employee.exceptions.JsonMappingException;
import com.employee.logger.EmployeeLogger;
import com.employee.pojo.Employee;
import com.employee.pojo.EmployeeAndUserResponse;
import com.employee.pojo.EmployeeResponse;
import com.employee.pojo.EmployeeResponseWithId;
import com.employee.pojo.FirstnameDetails;
import com.employee.pojo.Users;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.userlogin.dao.CredentialsDao;



/*Service class represents the web service, This will consume the requests made to the server and pass intformation to DAO*/
@Path("/employees")
public class EmployeeService{
	
	static Logger logger = Logger.getLogger(EmployeeLogger.class);

			
	@EJB
	RequestLoggingBean requestLoggingBean;
	
	@EJB
	ValidationBean validatonBean;
	
	@EJB
	CredentialsDao credentialsDao;
	
	@Context 
	private HttpHeaders httpHeaders;
	@Context 
	private HttpServletRequest request;
	
	private static EmployeeLogger employeeLogger = new EmployeeLogger();

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/addemployee") /*Adding via webservice URL http://localhost:8443/Employee/rest/employees/addemployee*/
	public Response addEmployee (String requestBody) throws Exception {
		logger.info("----Adding new employee via web service----\n");
		logger.info("Request body: " + requestBody);
		
		
		Employee employee = deserializeMapper(requestBody);
		
		employeeLogger.logIncomingEmployee(request, requestBody);
		authType(AuthEnum.SHA256, httpHeaders, requestBody);
		requestLoggingBean.logRequestToDatabase(employee);
		
		Response response = Validation(validatonBean, employee);
		return response;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getallemployees") // http://localhost:8080/Employee/rest/employees/getallemployees
	public Response getAllEmployees () {
		logger.info("----Getting all employee's from database----");
		
		authType(AuthEnum.BASIC, httpHeaders, "");
		List<TblEmployee> employeeList = requestLoggingBean.readAllEmployees();
		return Response.status(Response.Status.OK).entity(employeeList).build();
		
	}
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getallemployeeswithfirstname/{firstName}") // http://localhost:8080/Employee/rest/employees/getcustomemployee/Fred
	public Response getAllEmployeesWithFirstName (@PathParam("firstName") String empFirstname) { 
		logger.info("----Retreiving Information about Specific Employee's");
		
		List<TblEmployee> employeeList = requestLoggingBean.readAllEmployeesWithFirstName(empFirstname);
		return Response.status(Response.Status.OK).entity(employeeList).build();
	}
	
	
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/removeEmployee/{id}") //http://localhost:8080/Employee/rest/employees/removeEmployee/26
	public Response removeEmployee (@PathParam("id") int idPassedByService) {
		logger.info("----Deleting employee with id: " + idPassedByService + " from database----");
		
		requestLoggingBean.deleteEmployeeById(idPassedByService);
		return Response.status(Response.Status.OK).build();
	}
	
	
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/editEmployee/{id}") //http://localhost:8080/Employee/rest/employees/editEmployee/20
	//TODO Remove throws
	public Response editEmployee (@PathParam("id") int idPassedByService, String requestBody) throws IOException {
		logger.info("----Updating employee from database----");
		
		Employee employee = deserializeMapper(requestBody);
		requestLoggingBean.updateEmployee(idPassedByService, employee);
		Response response = Validation(validatonBean, employee);
		
		return response;
		
	}
	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/firstname/{id}")
	public Response getFirstnameById (@PathParam("id") Integer id) {
		
		//RequestLoggingBean requestLoggingBean = new RequestLoggingBean();
		String employee = requestLoggingBean.getFirstnameByIdDao(id);
		
		return Response.status(Response.Status.OK).entity(employee).build();
	}
	
	
	
	/*-------------------Just testing some random services out from here onwards--------------------*/
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/multipletables/employeeid/{employeeid}/userid/{userid}")
	public Response getFirstNameAndUserByIdUsing2DBTables (@PathParam("employeeid") Integer employeeId, @PathParam("userid") String userId) {
		
		EmployeeAndUserResponse employeeAndUserResponse = requestLoggingBean.getFirstnameAndUserById(employeeId, userId);
		Response response = successfulResponseEmployeeAndUser(employeeAndUserResponse);
		
		return response;
	}
	
	/*Similar to the above service, but wraps it within its own object.*/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/object/employeeid/{employeeid}/userid/{userid}")
	public Response getFirstNameAndUserByIdWithObject (@PathParam("employeeid") Integer employeeId, @PathParam("userid") String userId) {
		
		FirstnameDetails FirstnameDetailsResponse = requestLoggingBean.getFirstnameAndUserByIdWithObjectName(employeeId, userId);
		Response response = successfulFirstnameDetailsResponse(FirstnameDetailsResponse);
		
		
		return response;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filtered")
	public Response testingQueryParams (@QueryParam("start") int startId) { 
		
		List<TblEmployee> response = requestLoggingBean.GreaterThanListOfEmployeeById(startId);
		return Response.status(Response.Status.OK).entity(response).build();
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/filteredwithid")
	public Response testingQueryParamsWithIdInResponse (@QueryParam("start") int startId) { 
		
		List<TblEmployee> employeeList = requestLoggingBean.GreaterThanListOfEmployeeByIdWithId(startId);
		Response response = successfulResponseWithId(employeeList);
		

		return response;
	}
	
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/createuser")
	public Response addUser (String requestBody) throws IOException {
		
		//basic = new BasicAuthentication();
		//basic.authenticate(httpHeaders, employee);
		
		Users user = deserializeMapperUsers(requestBody);
		requestLoggingBean.createUser(user);
		
		return Response.status(204).build();
	}
	
	
	
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Path("/headers")
	public String getHeaders (@Context HttpHeaders httpHeaders) {
			
	      MultivaluedMap<String, String> rh = httpHeaders.getRequestHeaders();
	      String str = rh.entrySet()
	                     .stream()
	                     .map(e -> e.getKey() + " = " + e.getValue()) //e = entry in the map. for every entry get the key and the value.
	                     .collect(Collectors.joining("\n"));
	      return str;
	  
	}
	
	
	
	
	private Response Validation (ValidationBean validationBean, Employee employee) {
		
		Boolean checkinMandatoryFields = validationBean.validateIncomingData(employee);
		if (checkinMandatoryFields) {
			return failedResponseBadRequest();
		}
		
		return successfulResponse(employee);
		
	}
	
	
	private void authType (AuthEnum authType, HttpHeaders httpHeaders, String requestBody) {
		if (authType == AuthEnum.SHA256) {
			AuthenticationType authSha256 = new Hmac256Authentication();
			authSha256.authenticate(httpHeaders, requestBody);
		}
		
		if (authType == AuthEnum.BASIC) {
			AuthenticationType authBasic = new BasicAuthentication(credentialsDao.getAllCredentials());
			authBasic.authenticate(httpHeaders, requestBody);
		}
	}
	
	
	private Response successfulResponse (Employee employee) {
		EmployeeResponse successResponse = new EmployeeResponse();
		
		successResponse.setFirstName(employee.getFirstName());
		successResponse.setLastName(employee.getLastName());
		successResponse.setPhoneNumber(employee.getPhoneNumber());
		
		String successJson = serializeResponseJson(successResponse);
		return Response.status(Response.Status.OK).entity(successJson).build();
	}
	
	
	private Response successfulResponseEmployeeAndUser (EmployeeAndUserResponse employeeAndUserResponse) {
		String successJson = serializeResponseJsonEmployeeUser(employeeAndUserResponse);
		return Response.status(Response.Status.OK).entity(successJson).build();
	}
	
	
	private Response successfulFirstnameDetailsResponse (FirstnameDetails firstnameDetails) {
		String successJson = serializeResponseFirstnameDetails(firstnameDetails);
		return Response.status(Response.Status.OK).entity(successJson).build();
	}
	
	private Response successfulResponseWithId (List<TblEmployee> employeeList) {
		
		List<EmployeeResponseWithId> empResponseWithIdList = new ArrayList<>();
		
		for (TblEmployee employee : employeeList) {
			EmployeeResponseWithId empResponseWithId = new EmployeeResponseWithId();
			
			empResponseWithId.setId(employee.getId());
			empResponseWithId.setFirstName(employee.getFirstName());
			empResponseWithId.setLastName(employee.getLastName());
			empResponseWithId.setPhoneNumber(employee.getPhoneNumber());
			
			empResponseWithIdList.add(empResponseWithId);
			
		}
		
		String successJson = serializeResponseWithId(empResponseWithIdList);
		return Response.status(Response.Status.OK).entity(successJson).build();
	}
	
	
	private Response failedResponseBadRequest () {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}
	
	
	private Response failedResponseUnauthorized () {
		return Response.status(Response.Status.UNAUTHORIZED).build();
	}
	
	
	//TODO Remove throws and fix
	private Employee deserializeMapper(String requestBody) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Employee employee;
		try {
			employee = mapper.readValue(requestBody, Employee.class);
		} catch (JsonMappingException e) {
			throw new JsonMappingException("Failed to map incoming JSON", e);
		} catch(JsonProcessingException e) {
			throw new JsonMappingException("Failed to process incoming JSON", e);
		}
		
		return employee;
	}
	
	private Users deserializeMapperUsers(String requestBody) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Users users;
		try {
			users = mapper.readValue(requestBody, Users.class);
		} catch (JsonMappingException e) {
			throw new JsonMappingException("Failed to map incoming JSON", e);
		} catch(JsonProcessingException e) {
			throw new JsonMappingException("Failed to process incoming JSON", e);
		}
		
		return users;
	}

	
	private String serializeResponseJson (EmployeeResponse response) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
		} catch (JsonProcessingException e) {
			throw new JsonMappingException("Failed to serialise response JSON", e);
		}
		
		return jsonString;
	}
	
	private String serializeResponseJsonEmployeeUser (EmployeeAndUserResponse response) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
		} catch (JsonProcessingException e) {
			throw new JsonMappingException("Failed to serialise response JSON", e);
		}
		
		return jsonString;
	}
	
	private String serializeResponseFirstnameDetails (FirstnameDetails response) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
		} catch (JsonProcessingException e) {
			throw new JsonMappingException("Failed to serialise response JSON", e);
		}
		
		return jsonString;
	}
	
	private String serializeResponseWithId (List<EmployeeResponseWithId> response) {
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonString;
		
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
		} catch (JsonProcessingException e) {
			throw new JsonMappingException("Failed to serialise response JSON", e);
		}
		
		return jsonString;
	}
	
	
	
	
	
	
	/*-----------------------------------------------------------------------------------------------*/
	

	
	private static String getMessageIdHeader(@Context HttpHeaders httpHeaders) {
		String messageId = httpHeaders.getRequestHeader("MessageId").get(0);
		logger.info("Message ID Header: " + messageId);
		return messageId;
	}
	
	/*Unused*/
	private static String getAuthHeaderServ(HttpServletRequest request) {
		logger.info("enters getAuthHeaderServ");
		String auth = request.getHeader	("Authorization").toLowerCase();
		logger.info("Creates Auth: " + auth);
		logger.info("NEW AUTH HEADER: " + request);
		return auth;
	}
	
	//Get all request headers
	//http://localhost:8080/Employee/rest/employees/headers

	

	

}
