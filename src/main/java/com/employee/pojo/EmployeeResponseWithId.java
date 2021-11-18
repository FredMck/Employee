package com.employee.pojo;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;


@JsonPropertyOrder({"id", "firstName", "lastName", "phoneNumber"})
public class EmployeeResponseWithId {

	
		private int id;
		private String firstName;
		private String lastName;
		private String phoneNumber;
		
		
		@JsonProperty("id")
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		@JsonProperty("firstName")
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		
		@JsonProperty("lastName")
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		@JsonProperty("phoneNumber")
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	
	
		@Override
		public String toString() {
			return "id= " + id + "firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
					+ phoneNumber;
		}
	
	
}
