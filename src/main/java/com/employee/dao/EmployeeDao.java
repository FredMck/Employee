package com.employee.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.Query;

import org.hibernate.HibernateError;
import org.hibernate.HibernateException;
//import org.hibernate.Query;

import com.employee.entity.TblExtraEmployeeInfo;
import com.employee.model.EntityManagerUtil;
import com.employee.pojo.Employee;
import com.employee.pojo.EmployeeAndUserResponse;
import com.employee.pojo.EmployeeResponse;
import com.employee.pojo.EmployeeResponseWithId;
import com.employee.pojo.FirstnameDetails;
import com.employee.pojo.GetEmployeeFirstnameById;
import com.employee.pojo.Users;
import com.employee.service.EmployeeService;


/*DAO for Data Access Object - Here we are interacting with the database*/
public class EmployeeDao {
	
	private final EntityManager em;
	
	public EmployeeDao() {
		em = EntityManagerUtil.getEntityManager();
	}

	
	//Create Employee Record In Database
	public void createEmployee(Employee employeeDetails) {
		
		//EntityManager em = EntityManagerUtil.getEntityManager();
		
		System.out.println("Starting Transaction");
		
		em.getTransaction().begin();
		System.out.println("1) Employee FirstName " + employeeDetails.getFirstName());
		System.out.println("2) Employee LastName " + employeeDetails.getLastName());
		System.out.println("3) Employee PhoneNumber " + employeeDetails.getPhoneNumber());
		System.out.println("Entering Employee Into Database");
		
		try {
			System.out.println("persisted");
			em.persist(employeeDetails);
		} catch (HibernateException he) {
			System.out.println("rolledback");
			em.getTransaction().rollback();
		}
		
		System.out.println("committing transaction");
		em.getTransaction().commit();
		
		
		TblExtraEmployeeInfo tblExtraEmp = new TblExtraEmployeeInfo();
		tblExtraEmp.setEmployee_idEntity(employeeDetails.getId());
		tblExtraEmp.setSalaryEntity(17000.00);
		tblExtraEmp.setAgeEntity(19);
		
		insertExtraEmployeeInfo(tblExtraEmp);
		
		
		
		System.out.println("Employee in Database");
		em.close();
		
		
		
		System.out.println("Connection Closed");
		
		
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
	
	public void createUser (Users user) {
		
		em.getTransaction().begin();
		
		try {
			em.persist(user);
		} catch (HibernateException he){
			em.getTransaction().rollback();
		}
		
		em.getTransaction().commit();
		em.close();
	}
	
	
		// Get all Employees  From Database
		public List<Employee> readAllEmployees() {
			
		//EntityManager em = EntityManagerUtil.getEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Employee e", Employee.class);
		List<Employee> employees = query.getResultList();
		em.getTransaction().commit();
		em.close();        
		
		return employees;
	    
	}
		
		//Get Custom Employees from Database
		public List<Employee> readCustomEmployee(String first_name) {
			
			System.out.println("Entered ReadCustomEmployee Method: ");
			
			//EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			/*Employee employee = new Employee();
			employee.setEmployeeFirstName(employeeFirstname);*/
			
			
			Query query = em.createQuery("from Employee e where first_name = :first_name", Employee.class);
			query.setParameter("first_name", first_name);
			List<Employee> employees = query.getResultList();
			em.getTransaction().commit();
			em.close();
			
			return employees;
		}
		
		public GetEmployeeFirstnameById getFirstnameByIdDao (Integer id) {
			
			//EntityManager em = EntityManagerUtil.getEntityManager();
			
			em.getTransaction().begin();
			TypedQuery<String> query = em.createNamedQuery("Employee.getFirstnameById", String.class);
					query.setParameter("id", id);
			
			//Employee employee = new Employee();
			Employee emp = em.find(Employee.class, id);
			GetEmployeeFirstnameById empByFirstname = new GetEmployeeFirstnameById();
			empByFirstname.setFirstName(emp.getFirstName());
			
			em.getTransaction().commit();
			em.close();
			
			return empByFirstname;
		}
		
		
		public List<EmployeeResponse> GreaterThanListOfEmployeeById (int id) {
			
			em.getTransaction().begin();
			TypedQuery<Employee> query = em.createNamedQuery("Employee.GreaterThanListById", Employee.class);
			query.setParameter("id", id);
			
			//Employee emp = em.find(Employee.class, id);
			//Employee emp = em.getReference(Employee.class, id);
			List<EmployeeResponse> responseList = new ArrayList<>();
			
			for (Employee employee : query.getResultList()) { 
				EmployeeResponse employeeResponse = new EmployeeResponse();
				employeeResponse.setFirstName(employee.getFirstName());
				employeeResponse.setLastName(employee.getLastName());
				employeeResponse.setPhoneNumber(employee.getPhoneNumber());
				
				responseList.add(employeeResponse);
			}
			
			return responseList;
			
		}
		
		public List<EmployeeResponseWithId> GreaterThanListOfEmployeeByIdWithId (int id) {
			
			em.getTransaction().begin();
			TypedQuery<Employee> query = em.createNamedQuery("Employee.GreaterThanListById", Employee.class);
			query.setParameter("id", id);
			
			//Employee emp = em.find(Employee.class, id);
			//Employee emp = em.getReference(Employee.class, id);
			List<EmployeeResponseWithId> responseList = new ArrayList<>();
			
			for (Employee employee : query.getResultList()) { 
				EmployeeResponseWithId employeeResponse = new EmployeeResponseWithId();
				employeeResponse.setId(employee.getId());
				employeeResponse.setFirstName(employee.getFirstName());
				employeeResponse.setLastName(employee.getLastName());
				employeeResponse.setPhoneNumber(employee.getPhoneNumber());
				
				responseList.add(employeeResponse);
			}
			
			
			return responseList;
			
		}
		
		public EmployeeAndUserResponse getFirstnameAndUserById (Integer employeeId, String userId) {
			
			em.getTransaction().begin();
			
			TypedQuery<String> Employeequery = em.createNamedQuery("Employee.getFirstnameById", String.class);
			Employeequery.setParameter("id", employeeId);
			TypedQuery<String> UserQuery = em.createNamedQuery("Users.getUsernameById", String.class);
			UserQuery.setParameter("user_id", userId);
			
			Employee emp = em.find(Employee.class, employeeId);
			Users user = em.find(Users.class, userId);
			
			EmployeeAndUserResponse employeeUser = new EmployeeAndUserResponse();
			
			employeeUser.setFirstname(emp.getFirstName());
			employeeUser.setUsername(user.getUsername());
			
			em.getTransaction().commit();
			em.close();
			
			return employeeUser;
		}
		
		
		public FirstnameDetails getFirstnameAndUserByIdWithObjectName (Integer employeeId, String userId) {
			
			em.getTransaction().begin();
			
			TypedQuery<String> Employeequery = em.createNamedQuery("Employee.getFirstnameById", String.class);
			Employeequery.setParameter("id", employeeId);
			TypedQuery<String> UserQuery = em.createNamedQuery("Users.getUsernameById", String.class);
			UserQuery.setParameter("user_id", userId);
			
			Employee emp = em.find(Employee.class, employeeId);
			Users user = em.find(Users.class, userId);
			
			FirstnameDetails firstnameDetails = new FirstnameDetails();
			EmployeeAndUserResponse employeeUser = new EmployeeAndUserResponse();
			
			employeeUser.setFirstname(emp.getFirstName());
			employeeUser.setUsername(user.getUsername());
			
			firstnameDetails.setEmployeeAndUserResponse(employeeUser);
			
			em.getTransaction().commit();
			em.close();
			
			return firstnameDetails;
		}
		
		
		
		// Deletes an employee from database by ID
		public void deleteEmployee (int id) { 
			
			//EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			Employee employee = new Employee();
			Employee emp = em.find(Employee.class, id);
			
			em.remove(emp);
			System.out.println("Removed employee: " + employee.getFirstName() + " " + employee.getLastName() + " From database with id: " + id);
			
			em.getTransaction().commit();
			em.close();  
			
		}
		
		// Updates an Employee from database
		public void updateEmployee (int id, Employee employeeDetails) {
			
			//EntityManager em = EntityManagerUtil.getEntityManager();
			em.getTransaction().begin();
			
			
			String firstName = employeeDetails.getFirstName();
			String lastName = employeeDetails.getLastName();
			String phoneNumber = employeeDetails.getPhoneNumber();
			
			employeeDetails = em.find(Employee.class, id);
			
			
			employeeDetails.setFirstName(firstName);
			employeeDetails.setLastName(lastName);
			employeeDetails.setPhoneNumber(phoneNumber);

			em.getTransaction().commit();
			em.close();
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
