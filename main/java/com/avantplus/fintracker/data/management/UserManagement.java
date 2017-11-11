package com.avantplus.fintracker.data.management;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import com.avantplus.fintracker.data.entity.User;

public class UserManagement {
private SessionFactory factory;
	
	public UserManagement(){
		factory = DBSessionFactory.getFactory();
	}

	public List<User> getUsers(){
		List <User> userList = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			userList = session.createQuery("FROM User").list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		return userList;
	}
	
	public User getUserByUsername(String userName) {
		List <User> userList = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			userList = session.createQuery("FROM User where userName='"+userName+"'").list();
			transaction.commit();
			 
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		
		User user = userList.get(0);
		
		return user;
	}
	
	public static void main(String [] args) {
		UserManagement um = new UserManagement();
		/*User pt = new User();
		pt.setAmountLimit(35890.00);
		pt.setBankName("HSBC");
		pt.setCreditMonthlyInterestRate(20.3);
		pt.setCutDate(new Date(2017,10,15));
		pt.setName("Credit");
		pt.setPayDateLimit(new Date(2019,12,31));
		pt.setPaymentTypeId(1);
		pt.setUserId(1);
		ptm.updatePaymentType(pt);
		Integer ptID = ptm.addPaymentType(pt);
		System.out.println("Payment created:"+ptID);*/
		/*List <User> users = um.getUsers();
		for (Iterator<User>  iterator = users.iterator(); iterator.hasNext();){
			User user  = iterator.next(); 
			System.out.println("User ID: " + user.getUserId());
			System.out.println("User username: " + user.getUserName());
			System.out.println("User pass: " + user.getPassword());
			System.out.println("User desc: " + user.getDescription());
			System.out.println("------------------------------------------------------");
	
	    }*/
		User user = um.getUserByUsername("jorge_sosa_hdz@yahoo.com");
		System.out.println("User ID: " + user.getUserId());		
		
	}
}