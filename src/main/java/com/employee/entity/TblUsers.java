package com.employee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "users")
@NamedQueries({
@NamedQuery(name="TblUsers.getUsernameById", query="select u.username from TblUsers u where u.user_id = :user_id"),
@NamedQuery(name="TblUsers.getByUserId", query="select u from TblUsers u where u.user_id = :user_id"),
@NamedQuery(name="TblUsers.getUserByUsername", query="select u from TblUsers u"),
})
public class TblUsers {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private String user_id;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	
	

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
