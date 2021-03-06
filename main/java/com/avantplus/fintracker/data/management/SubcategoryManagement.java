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
import com.avantplus.fintracker.data.entity.Subcategory;

public class SubcategoryManagement {
	
private SessionFactory factory;
	
	public SubcategoryManagement(){
		factory = DBSessionFactory.getFactory();
	}
	
	public List<Subcategory> getSubcategories(){
		List<Subcategory> categorySubTypeList = null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			categorySubTypeList  = new ArrayList<Subcategory>();
			categorySubTypeList = session.createQuery("FROM Subcategory where isDeleted=0").list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return categorySubTypeList;
	}
	
	public List<Subcategory> getSubcategoriesByUserId(int userId){
		List<Subcategory> categorySubTypeList = null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			categorySubTypeList  = new ArrayList<Subcategory>();
			Query query = session.createQuery("FROM Subcategory where isDeleted=0 and (userId="+userId+" or userId=0)");
			categorySubTypeList = query.list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return categorySubTypeList;
	}
	
	public Subcategory getSubcategoryById(int subcategoryId){
		Subcategory subcategory = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query =  session.createQuery("Select subcategoryId, categoryId, name, userId FROM Subcategory where isDeleted=0 and "+
			"subcategoryId="+subcategoryId);
			Object[] objects = (Object [])query.uniqueResult();
			subcategory = new Subcategory();
			
			//check if results were returned
			if (objects == null)
				return subcategory;
			
			subcategory.setSubcategoryId(new Integer(objects[0].toString()).intValue());
			subcategory.setCategoryId(new Integer(objects[1].toString()).intValue());
			subcategory.setName(objects[2].toString());
			subcategory.setUserId(new Integer(objects[3].toString()).intValue());
			transaction.commit();			
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
	return subcategory;
	}
		
	public Subcategory addSubcategory(Subcategory subcategory) throws ConstraintViolationException{
		Session session = factory.openSession();
		
		boolean onException = false;
	      Transaction tx = null;
	      Integer subcategoryId = null;
	      try{
	         tx = session.beginTransaction();
	         subcategoryId = (Integer) session.save(subcategory); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	         onException = true;
	      }finally {
	         session.close(); 
	      }
	      //Check if an exception ocurred
	      if (onException)
	    	  throw new ConstraintViolationException("Unique Constraint", new SQLException(),"Subcategry Exists");
	      
	      return this.getSubcategoryById(subcategoryId);
	}
	
	public Subcategory updateSubcategory(Subcategory subcategory) throws PersistenceException{
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.update(subcategory); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      
	      return this.getSubcategoryById(subcategory.getSubcategoryId());
	}
	
	public int deleteSubcategory(int subcategoryId) {
		Session session = factory.openSession();
		int deleted_entities = 0;
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("UPDATE Subcategory set isDeleted=1 where subcategoryId=:categoryId "); 
	         query.setParameter("categoryId", subcategoryId);
	         deleted_entities += query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return deleted_entities;
	}
	
	
	public static void main(String [] args) {
		SubcategoryManagement cm = new SubcategoryManagement();
		//Add category
		Subcategory s_cat = new Subcategory();
		s_cat.setSubcategoryId(69);
		s_cat.setCategoryId(18);
		s_cat.setName("Sub Cat 1 UPDATED");
		s_cat.setUserId(1);
		
		//cm.updateSubcategory(s_cat);
		cm.deleteSubcategory(69);
	
		List<Subcategory> catList = cm.getSubcategories();
		for (Iterator<Subcategory> iterator = 
				catList.iterator(); iterator.hasNext();){
				Subcategory cat = iterator.next(); 
				System.out.println("Cat SubType ID: " + cat.getSubcategoryId());
				System.out.println("Cat ID: " + cat.getCategoryId());
				System.out.println("Cat Name: " + cat.getName());
				System.out.println("Is Deleted: " + cat.getIsDeleted());
		 
				 
		}
	}
}
