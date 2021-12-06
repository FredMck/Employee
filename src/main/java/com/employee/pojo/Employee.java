package com.employee.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;




@JsonPropertyOrder({"firstName", "lastName", "phoneNumber"})
public class Employee {
	
	
		private int id;
		private String firstName;
		private String lastName;
		private String phoneNumber;
		
		
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
