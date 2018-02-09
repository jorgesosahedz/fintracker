package com.avantplus.fintracker.data.management;

import java.util.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import com.avantplus.fintracker.data.entity.PaymentType;

public class PaymentTypeManagement {
	
	private SessionFactory factory;	
	public PaymentTypeManagement(){
		factory = DBSessionFactory.getFactory();
	}

	public List<PaymentType> getPaymentTypes(){
		List <PaymentType> paymentTypeList = null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			paymentTypeList  = new ArrayList<PaymentType>();
			paymentTypeList = session.createQuery("FROM PaymentType where isDeleted=0").list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return paymentTypeList;
	}
	
	public List<PaymentType> getPaymentTypesByUserId(int userId){
		List <PaymentType> paymentTypeList = null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			paymentTypeList  = new ArrayList<PaymentType>();
			paymentTypeList = session.createQuery("FROM PaymentType where isDeleted=0 and userId="+userId).list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return paymentTypeList;
	}
	
	public PaymentType getPaymentTypeById(int paymentTypeId){
		PaymentType payment = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Query query =  session.createQuery("Select paymentTypeID,userId,name,bankName,cutDate,payDateLimit,amountLimit,creditMonthlyInterestRate FROM PaymentType where isDeleted=0 and "+
			"paymentTypeID="+paymentTypeId);
			Object[] objects = (Object [])query.uniqueResult();
			payment = new PaymentType();
			payment.setPaymentTypeID(new Integer(objects[0].toString()).intValue());
			payment.setUserId(new Integer(objects[1].toString()).intValue());
			payment.setName(objects[2].toString());
			payment.setBankName(objects[3].toString());
			payment.setCutDate((Timestamp)objects[4]);
			payment.setPayDateLimit((Timestamp)objects[5]);
			payment.setAmountLimit(new Double(objects[6].toString()).doubleValue());
			payment.setCreditMonthlyInterestRate(new Double(objects[7].toString()).doubleValue());
			transaction.commit();			
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
	return payment;
	}
	
	public PaymentType addPayment(PaymentType paymentType) throws ConstraintViolationException {
		Session session = factory.openSession();
		boolean onException = false;
	      Transaction tx = null;
	      Integer paymentTypeId = null;
	      
	      try{
	         
	    	 tx = session.beginTransaction();
	         paymentTypeId = (Integer) session.save(paymentType); 
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
	      
	      return this.getPaymentTypeById(paymentTypeId);
	}
	
	public PaymentType updatePaymentType(PaymentType paymentType)  throws PersistenceException {
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.update(paymentType); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      
	     return this.getPaymentTypeById(paymentType.getPaymentTypeID());
	}
	
	public int deletePaymentType(int paymentTypeId) {
		Session session = factory.openSession();
		int deleted_entities = 0;
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("UPDATE PaymentType set isDeleted=1 where paymentTypeID=:paymentTypeId "); 
	         query.setParameter("paymentTypeId", paymentTypeId);
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
	
	public static void main(String [] args ){
		PaymentTypeManagement cm = new PaymentTypeManagement();
		PaymentType pt = new PaymentType();
		pt.setPaymentTypeID(8);
		pt.setUserId(1);
		pt.setName("Updated Payment Type");
		pt.setAmountLimit(3000.00);
		pt.setBankName("Santander");
		pt.setCreditMonthlyInterestRate(13.24);
		pt.setCutDate((Date)Timestamp.valueOf("2017-11-07 00:00:00"));
		pt.setPayDateLimit((Date)Timestamp.valueOf("2017-11-07 00:00:00"));
		cm.deletePaymentType(8);
		List<PaymentType> list = cm.getPaymentTypesByUserId(1);
		for (Iterator<PaymentType> iterator =  list.iterator(); iterator.hasNext(); ) {
			PaymentType pay =iterator.next(); 
			System.out.println("Payment Type:"+pay.getPaymentTypeID());
			System.out.println("Payment Type:"+pay.getName());
			System.out.println("User ID:"+pay.getUserId());
		}
		
	}
	
}
