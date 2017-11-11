package com.avantplus.fintracker.data.management;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.avantplus.fintracker.data.entity.*;


public class TransactionManagement {
	private SessionFactory factory;
	
	public TransactionManagement(){
		factory = DBSessionFactory.getFactory();
	}
	
	public List getTransactions(){
		List transactionList = null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			transactionList  = new ArrayList();
			transactionList = session.createQuery("select tx.transactionID, cat.name,cst.name, pay.name, tx.amount, \r\n" + 
					"tx.timeStamp, tx.description\r\n" + 
					"from UserTransactionBase as tx, Category as cat, Subcategory cst, PaymentType as pay\r\n" + 
					"where tx.paymentTypeId = pay.paymentTypeID and\r\n" + 
					"tx.subcategoryId = cst.subcategoryId and\r\n" + 
					"cst.categoryId = cat.categoryId \r\n"
				     ).list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return transactionList;
	}
	
	public List<UserTransactionBase> getTransactions(int userId){
		List transactionList 		= null;
		List<UserTransactionBase> transactionListFinal 	= null;
		 Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			transactionList  		= new ArrayList();
			transactionList 		= session.createQuery("select tx.transactionID, tx.userId, \r\n"+
			"tx.subcategoryId, tx.paymentTypeId, tx.amount, tx.timeStamp, tx.description \r\n" + 
			"from UserTransactionBase as tx \r\n" + 
			"where \r\n"+
			"tx.userId ="+userId).list();
			
			transaction.commit();
			
			transactionListFinal = new ArrayList();
			for(Iterator i = transactionList.iterator();i.hasNext();) {
				Object [] objects = (Object []) i.next();
				UserTransactionBase utb = new UserTransactionBase();
				utb.setTransactionId(new Integer(objects[0].toString()).intValue());
				utb.setUserId(new Integer(objects[1].toString()).intValue());
				utb.setSubCategoryId(new Integer(objects[2].toString()).intValue());
				utb.setPaymentTypeId(new Integer(objects[3].toString()).intValue());
				utb.setAmount(new Double(objects[4].toString()).doubleValue());
				utb.setTimeStamp((Timestamp)(objects[5]));
				
				//verify if description is null
				String description = (String) objects[6];
				if (description != null)
					utb.setDescription(description);
				else
					utb.setDescription("");
				
				//Add to return list
				transactionListFinal.add(utb);
			}
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return transactionListFinal;
	}
	
	public Integer addTransaction(UserTransactionBase transaction) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      Integer transactionId = null;
	      try{
	         tx = session.beginTransaction();
	         transactionId = (Integer) session.save(transaction); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	      return transactionId;
	}
	
	public void updateTransaction(UserTransactionBase transaction) {
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         session.update(transaction); 
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	}
	
	
	public void deleteTransaction(UserTransactionBase transaction) {
		int transactionId = transaction.getTransactionId();
		Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Query query = session.createQuery("UPDATE UserTransactionBase set isDeleted=1 where transactionId=:transactionId "); 
	         query.setParameter("transactionId", transactionId);
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
		TransactionManagement tm = new TransactionManagement();
		UserTransactionBase ut = new UserTransactionBase();
		ut.setTransactionId(259);
		ut.setAmount(270.35);
		ut.setDescription("UPDATE TEST");
		ut.setPaymentTypeId(1);
		ut.setSubCategoryId(32);
		ut.setUserId(1);
		tm.updateTransaction(ut);
		/*List allRecords = tm.getTransactions(10);
		for (Iterator iterator0 = allRecords.iterator(); iterator0.hasNext();){
			Object [] record = (Object [])iterator0.next();
			for(int i = 0; i <= record.length - 1;i++) {
				System.out.println("Transaction:"+record[i]);
				//System.out.println("Transaction ID"+list.get(0));	 
			}
		}*/
	}
}
