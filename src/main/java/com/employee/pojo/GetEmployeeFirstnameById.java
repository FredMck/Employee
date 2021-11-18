package com.employee.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

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
