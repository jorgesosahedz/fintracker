package com.avantplus.fintracker.data.management;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
			categorySubTypeList = session.createQuery("FROM Subcategory where isDeleted=0 and userId="+userId).list();
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
		
	public Integer addSubcategory(Subcategory subcategory) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      Integer categorysubTypeId = null;
	      try{
	         tx = session.beginTransaction();
	         categorysubTypeId = (Integer) session.save(subcategory); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return categorysubTypeId;
	}
	
	public void updateSubcategory(Subcategory subcategory) {
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
	}
	
	public void deleteSubcategory(int subcategoryId) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("UPDATE Subcategory set isDeleted=1 where subcategoryId=:categoryId "); 
	         query.setParameter("categoryId", subcategoryId);
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
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
