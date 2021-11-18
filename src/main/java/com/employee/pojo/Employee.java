package com.employee.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


@Entity
@Table(name = "employee_phonelist")
@JsonPropertyOrder({"firstName", "lastName", "phoneNumber"})
@NamedQueries({
@NamedQuery(name="Employee.getFirstnameById", query="select e.firstName from Employee e where e.id = :id"),
@NamedQuery(name="Employee.GreaterThanListById", query="select e from Employee e where e.id >= :id"),
})
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
	
		@Id
		@Column(name = "id")
		//@GeneratedValue(strategy = GenerationType.AUTO)
		private int id;
		/*On soapUI might need to change the request body to remove the underscore (Or change jsonproperty to include underscore)*/
		@Column(name = "first_name")
		private String firstName;
		@Column(name = "last_name")
		private String lastName;
		@Column(name = "phone_number")
		private String phoneNumber;
		
		/*public Employee() {
			
		}*/
		
		/*public Employee(int employeeId, String employeeFirstName, String employeeLastName, String employeePhoneNumber) {
			this.employeeId=employeeId;
			this.employeeFirstName=employeeFirstName;
			this.employeeLastName=employeeLastName;
			this.employeePhoneNumber=employeePhoneNumber;
		}*/
		
		/*@JsonCreator
		public Employee(String employeeFirstName, String employeeLastName, String employeePhoneNumber) {
			this.employeeFirstName=employeeFirstName;
			this.employeeLastName=employeeLastName;
			this.employeePhoneNumber=employeePhoneNumber;
		}*/
		
		@JsonIgnore
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		
		@JsonProperty(value = "firstName")
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		
		
		@JsonProperty(value = "lastName")
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		
		@JsonProperty(value = "phoneNumber")
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		
		@Override
		public String toString() {
			return "Id=" + id + ", FirstName=" + firstName
					+ ", LastName=" + lastName + ", PhoneNumber=" + phoneNumber;
		}		
		
}
