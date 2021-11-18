package com.userlogin.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.employee.model.EntityManagerUtil;
import com.employee.pojo.Employee;
import com.employee.pojo.Users;

public class CredentialsDao {

	
	public List<Users> getAllCredentials () {
		
		EntityManager em = EntityManagerUtil.getEntityManager();
		em.getTransaction().begin();
		
		Query query = em.createQuery("from Users u", Users.class);
		List<Users> users = query.getResultList();
		em.getTransaction().commit();
		em.close();        
		
		return users;
	}
	
}
