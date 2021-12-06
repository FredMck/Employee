package com.userlogin.dao;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.employee.entity.TblUsers;
import com.employee.pojo.Employee;
import com.employee.pojo.Users;

@Stateless
@LocalBean
public class CredentialsDao {
	
	@PersistenceContext(unitName = "office_phone_list")
	private EntityManager em;

	
	public List<TblUsers> getAllCredentials () {
		
		TypedQuery<TblUsers> query = em.createNamedQuery("TblUsers.getUserByUsername", TblUsers.class);
		List<TblUsers> users = query.getResultList();
		return users;
	}
	
}
