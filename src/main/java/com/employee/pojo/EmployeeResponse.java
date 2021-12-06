package com.employee.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"firstName", "lastName", "phoneNumber"})
public class EmployeeResponse {

	
		
		private String firstName;
		private String lastName;
		private String phoneNumber;
		
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
			return "firstName=" + firstName + ", lastName=" + lastName + ", phoneNumber="
					+ phoneNumber;
		}
}
