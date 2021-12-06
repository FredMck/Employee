package com.employee.dao.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
//import org.hibernate.Query;

import com.employee.entity.TblEmployee;
import com.employee.entity.TblExtraEmployeeInfo;
import com.employee.entity.TblUsers;
import com.employee.pojo.Employee;
import com.employee.pojo.EmployeeAndUserResponse;
import com.employee.pojo.EmployeeResponse;
import com.employee.pojo.EmployeeResponseWithId;
import com.employee.pojo.FirstnameDetails;
import com.employee.pojo.GetEmployeeFirstnameById;
import com.employee.pojo.Users;


/*DAO for Data Access Object - Here we are interacting with the database*/
@Stateless
@LocalBean
public class RequestLoggingBean {
	
	
	@PersistenceContext(unitName = "office_phone_list")
	private EntityManager em;
	

	public void logRequestToDatabase(Employee employee) {
		loggingIntoEmployeeTable(employee);
		//loggingIntoEmployeeExtraInfoTable(employee);
	}
	
	private void loggingIntoEmployeeTable(Employee employee) {
		
		System.out.println("Firstname = " + employee.getFirstName());
		
		TblEmployee tblEmployee = new TblEmployee();
		tblEmployee.setFirstName(employee.getFirstName());
		tblEmployee.setLastName(employee.getLastName());
		tblEmployee.setPhoneNumber(employee.getPhoneNumber());

		
		em.persist(tblEmployee);
		
	}
	
	private void loggingIntoEmployeeExtraInfoTable(Employee employee) {
		TblExtraEmployeeInfo tblExtraEmployeeInfo = new TblExtraEmployeeInfo();
		tblExtraEmployeeInfo.setEmployee_idEntity(employee.getId());
		tblExtraEmployeeInfo.setSalaryEntity(17000.00);
		tblExtraEmployeeInfo.setAgeEntity(19);
		
		em.persist(tblExtraEmployeeInfo);
	}
	
	
	
	
	// Get all Employees  From Database
	public List<TblEmployee> readAllEmployees() {
			
		Query query = em.createQuery("from TblEmployee e", TblEmployee.class);
		List<TblEmployee> employees = query.getResultList();  
		
		return employees;
    
	}
	
	//Get Custom Employees from Database with the same first name
	public List<TblEmployee> readAllEmployeesWithFirstName(String first_name) {
		
		Query query = em.createQuery("from TblEmployee e where first_name = :first_name", TblEmployee.class);
		query.setParameter("first_name", first_name);
		List<TblEmployee> employees = query.getResultList();
		
		return employees;
	}
	
	
	
	// Deletes an employee from database by ID
	public void deleteEmployeeById (int id) { 
		
		Query query = em.createNamedQuery("TblEmployee.DeleteById");
		query.setParameter("id", id).executeUpdate();
	}
	
	
	public String getFirstnameByIdDao (Integer id) {
		
		TypedQuery<String> query = em.createNamedQuery("TblEmployee.getFirstnameById", String.class);
				query.setParameter("id", id);
				
		String employee = query.getResultStream()
									.findFirst()
									.orElse(null);
		
		return employee;
	}
	
	
	public EmployeeAndUserResponse getFirstnameAndUserById (Integer employeeId, String userId) {
		
		TypedQuery<TblEmployee> EmployeeQuery = em.createNamedQuery("TblEmployee.GetById", TblEmployee.class);
		EmployeeQuery.setParameter("id", employeeId);
		TypedQuery<TblUsers> UserQuery = em.createNamedQuery("TblUsers.getByUserId", TblUsers.class);
		UserQuery.setParameter("user_id", userId);
		
		TblEmployee employee = EmployeeQuery.getResultStream()
											.findFirst()
											.orElse(null);
		
		TblUsers user = UserQuery.getResultStream()
									.findFirst()
									.orElse(null);
		
		EmployeeAndUserResponse employeeUser = new EmployeeAndUserResponse();
		
		employeeUser.setFirstname(employee.getFirstName());
		employeeUser.setUsername(user.getUsername());
		
		
		return employeeUser;
	}
	
	
	public FirstnameDetails getFirstnameAndUserByIdWithObjectName (Integer employeeId, String userId) {
		
		
		TypedQuery<String> EmployeeQuery = em.createNamedQuery("TblEmployee.getFirstnameById", String.class);
		EmployeeQuery.setParameter("id", employeeId);
		TypedQuery<String> UserQuery = em.createNamedQuery("TblUsers.getUsernameById", String.class);
		UserQuery.setParameter("user_id", userId);
		
		String employeeFirstname = EmployeeQuery.getResultStream()
											.findFirst()
											.orElse(null);

		String userName = UserQuery.getResultStream()
								.findFirst()
								.orElse(null);
		
		FirstnameDetails firstnameDetails = new FirstnameDetails();
		EmployeeAndUserResponse employeeUser = new EmployeeAndUserResponse();
		
		employeeUser.setFirstname(employeeFirstname);
		employeeUser.setUsername(userName);
		
		firstnameDetails.setEmployeeAndUserResponse(employeeUser);
		
		return firstnameDetails;
	}
	
	public List<TblEmployee> GreaterThanListOfEmployeeById (int id) {
		
		TypedQuery<TblEmployee> query = em.createNamedQuery("TblEmployee.GreaterThanListById", TblEmployee.class);
		query.setParameter("id", id);
		List<TblEmployee> employees = query.getResultList();
		
		return employees;
		
	}

	public List<TblEmployee> GreaterThanListOfEmployeeByIdWithId (int id) {
		
		TypedQuery<TblEmployee> query = em.createNamedQuery("TblEmployee.GreaterThanListById", TblEmployee.class);
		query.setParameter("id", id);
		List<TblEmployee> employees = query.getResultList();

		return employees;
		
	}
	
	
	public void createUser (Users user) {
		
		TblUsers tblUsers = new TblUsers();
		
		tblUsers.setUser_id(user.getUser_id());
		tblUsers.setUsername(user.getUsername());
		tblUsers.setPassword(user.getPassword());
		
		em.persist(tblUsers);

	}
	
	
	
	
	
	
	private void insertExtraEmployeeInfo (TblExtraEmployeeInfo extraEmployeeInfoEntity) {
		
		em.getTransaction().begin();
		
		System.out.println("empid: " + extraEmployeeInfoEntity.getEmployee_idEntity());
		System.out.println("salary: " + extraEmployeeInfoEntity.getSalaryEntity());
		System.out.println("age: " + extraEmployeeInfoEntity.getAgeEntity());

		
		try {
			System.out.println("extraEmp persisted");
			em.persist(extraEmployeeInfoEntity);
		} catch (HibernateException he) {
			System.out.println("rolledback");
			em.getTransaction().rollback();
		}
		
		em.getTransaction().commit();
		//em.close();
	}
	

	
	

		

		

	

	


	

	
	
	

	
	// Updates an Employee from database
	public void updateEmployee (int id, Employee employeeDetails) {
		
		/*Incoming details from request*/
		String firstName = employeeDetails.getFirstName();
		String lastName = employeeDetails.getLastName();
		String phoneNumber = employeeDetails.getPhoneNumber();
		
		/*Persisting data into table*/
		TblEmployee employeeTable = em.find(TblEmployee.class, id);
		employeeTable.setFirstName(firstName);
		employeeTable.setLastName(lastName);
		employeeTable.setPhoneNumber(phoneNumber);

		em.persist(employeeTable);
	}
	
	
	

	
	
/*		public Employee getFirstnameByIdDao (Integer id) {
			
			EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			System.out.println("ID: " + id);
			
			TypedQuery<String> query = em.createNamedQuery("Employee.getFirstnameById", String.class);
					query.setParameter("id", id);
			
			//Employee employee = new Employee();
			Employee emp = em.find(Employee.class, id);
			System.out.println("employee firstname; " + emp.getFirstName());		
			
			return emp;
		}
		*/
		

		
		
}
