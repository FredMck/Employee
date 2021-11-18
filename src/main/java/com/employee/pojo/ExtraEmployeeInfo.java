package com.employee.pojo;

import java.math.BigInteger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@JsonPropertyOrder({"employee_id", "salary", "age"})
public class ExtraEmployeeInfo {

	@JsonIgnore
	private int id;
	
	@JsonProperty(value = "employee_id")
	private int employee_id;
	
	@JsonProperty(value = "salary")
	private double salary;
	
	@JsonProperty(value = "age")
	private int age;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(int employee_id) {
		this.employee_id = employee_id;
	}
	
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
	
	
}
