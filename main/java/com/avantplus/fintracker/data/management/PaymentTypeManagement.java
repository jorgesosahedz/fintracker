package com.avantplus.fintracker.data.management;

import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
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
	
	public Integer addPayment(PaymentType paymentType) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      Integer paymentTypeId = null;
	      try{
	         tx = session.beginTransaction();
	         paymentTypeId = (Integer) session.save(paymentType); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return paymentTypeId;
	}
	
	public void updatePaymentType(PaymentType paymentType) {
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
	}
	
	public void deletePaymentType(int paymentTypeId) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("UPDATE PaymentType set isDeleted=1 where paymentTypeID=:paymentTypeId "); 
	         query.setParameter("paymentTypeId", paymentTypeId);
	         query.executeUpdate();
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
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
