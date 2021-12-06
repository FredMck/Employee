package com.employee.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetEmployeeFirstnameById {

		@JsonProperty
		private String firstName;

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		
	
}
