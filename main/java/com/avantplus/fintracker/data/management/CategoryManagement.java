package com.avantplus.fintracker.data.management;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import com.avantplus.fintracker.data.entity.Category;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;


public class CategoryManagement {
	private SessionFactory factory;
	
	public CategoryManagement(){
		factory = DBSessionFactory.getFactory();
	}
	
	public List<Category> getCategories(){
		List categoriesList = null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			categoriesList  = new ArrayList<Category>();
			categoriesList = session.createQuery("FROM Category where isDeleted = 0").list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
	return categoriesList;
	}
	
	public List<Category> getCategoriesbyUserId(int userId){
		List<Category> categoriesList1 		= null;
		List<Category> categoriesListFinal 	= null;
		
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			categoriesList1 =  session.createQuery("Select categoryId, name, userId FROM Category where isDeleted=0 and "+
			"userId="+userId+ "or userId=0").list();
			
			transaction.commit();
			
			categoriesListFinal = new ArrayList<Category>();
			for (Iterator i = categoriesList1.iterator(); i.hasNext(); ) {
				Object [] object = (Object [])i.next();
				Category udc = new Category();
				
				//Validate if results are returned
				if (object != null) {
				
					udc.setCategoryId(new Integer(object[0].toString()).intValue());
					udc.setName(object[1].toString());
					udc.setUserId(new Integer(object[2].toString()).intValue());
				}
				
				categoriesListFinal.add(udc);
			}
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
	return categoriesListFinal;
	}
	
	public Category getCategoryById(int categoryId){
		Category category = null;
		Session session = factory.openSession();
		
		Transaction transaction = null;
		try {
			
			transaction = session.beginTransaction();
			
			Query query =  session.createQuery("Select categoryId, name, userId FROM Category where isDeleted=0 and "+
			"categoryId="+categoryId);
			
			Object[] objects = (Object [])query.uniqueResult();
			
			transaction.commit();
			
			category = new Category();
			
			//Validate if results are returned
			if (objects != null) {
				category.setCategoryId(new Integer(objects[0].toString()).intValue());
				category.setName(objects[1].toString());
				category.setUserId(new Integer(objects[2].toString()).intValue());
			}	
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
	return category;
	}
	
	public Category addCategory(Category category) throws ConstraintViolationException{
		Session session = factory.openSession();
		boolean onException = false;
	      Transaction tx = null;
	      Integer categoryId = -1;
	      try{
	         tx = session.beginTransaction();
	         categoryId = (Integer) session.save(category); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) 
	        	 tx.rollback();
	         
	         e.printStackTrace(); 
	         
	         onException = true;
	      }finally {
	         session.close(); 
	      }
	      
	      //Check if an exception occurred
	      if (onException)
	    	  throw new ConstraintViolationException("Unique Constraint", new SQLException(),"Category Exists");
	    	 
	      return this.getCategoryById(categoryId);
	}
	
	
	public Category updateCategory(Category category) throws PersistenceException {
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.update(category); 
	         tx.commit();
	      }
	      catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace();
	      }
	      
	      finally {
	         session.close(); 
	      }
	      return this.getCategoryById(category.getCategoryId());
	}
	
	
	public int deleteCategory(int categoryId) {
		Session session = factory.openSession();
		int deleted_entities = 0;
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("UPDATE Category set isDeleted=1 where categoryId=:categoryId "); 
	         query.setParameter("categoryId", categoryId);
	         deleted_entities = query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return deleted_entities;
	}
	
	public static void main(String [] args ){
		CategoryManagement cm = new CategoryManagement();
		//Category cat = new Category();
		//cat.setName("New Cat 4");
		//cat.setUserId(1);
		//cat.setCategoryId(20);
		cm.deleteCategory(21);
		List<Category> list = cm.getCategories();
		for (Iterator<Category> iterator =  list.iterator(); iterator.hasNext(); ) {
			Category cat1 =iterator.next(); 
			System.out.println("My list:"+cat1.getName()+"--User ID"+cat1.getUserId());
		}
		
	}

}
