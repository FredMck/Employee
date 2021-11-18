package com.employee.entity;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.employee.pojo.Employee;


@Entity
@Table(name = "extra_employee_information")
public class TblExtraEmployeeInfo implements Serializable {
	
    private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idEntity;
	
	
	 //@JoinColumn(name = "id")
	@Column(name = "employee_id")
	//@JoinColumn(name="employee_id", referencedColumnName="id")
	//@OneToMany(cascade = CascadeType.ALL)
	private int employee_idEntity;
	
	@Column(name = "salary")
	private double salaryEntity;
	
	@Column(name = "Age")
	private int ageEntity;
	
	

	public int getIdEntity() {
		return idEntity;
	}

	public void setIdEntity(int idEntity) {
		this.idEntity = idEntity;
	}

	
	public int getEmployee_idEntity() {
		return employee_idEntity;
	}

	public void setEmployee_idEntity(int employee_idEntity) {
		this.employee_idEntity = employee_idEntity;
	}

	
	public double getSalaryEntity() {
		return salaryEntity;
	}

	public void setSalaryEntity(double salaryEntity) {
		this.salaryEntity = salaryEntity;
	}

	
	public int getAgeEntity() {
		return ageEntity;
	}

	public void setAgeEntity(int ageEntity) {
		this.ageEntity = ageEntity;
	}
	
	

	
	
	
	
	
}
