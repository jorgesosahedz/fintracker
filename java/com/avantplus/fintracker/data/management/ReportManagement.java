package com.avantplus.fintracker.data.management;
 
import com.avantplus.fintracker.data.models.*;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ReportManagement {

	private SessionFactory factory;
	
	public ReportManagement(){
		factory = DBSessionFactory.getFactory();
	}
	
	public List<ExpenseByMonth> getExpenseByMonth(int userId, int currentMonth, int currentYear, int periods) {
		List returnList = null;
		List<ExpenseByMonth> expenseList = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String my_query = "Select sum(tx.amount), month(tx.expenseDate), year(tx.expenseDate) from UserTransactionBase tx \r\n"+
					" where tx.userId ="+userId+" \r\n"+
					//" and month(tx.timeStamp) <="+currentMonth+" and year(tx.timeStamp) <="+currentYear+ "\r\n" +
					" group by month(tx.expenseDate), year(tx.expenseDate) \r\n"+ 
					" order by year(tx.expenseDate) desc, month(tx.expenseDate) desc";
						     
			Query query = session.createQuery(my_query);
			query.setMaxResults(periods);
			returnList  = query.list();
			transaction.commit();
			
			//Get results and put them into expense data model objects
			expenseList  = new ArrayList();
			for(Iterator i = returnList.iterator();i.hasNext();) {
				Object objects [] = (Object []) i.next();
				ExpenseByMonth expense = new ExpenseByMonth();
				expense.setAmount(Double.parseDouble(objects[0].toString()));
				expense.setMonth(Integer.parseInt(objects[1].toString()));
				expense.setYear(Integer.parseInt(objects[2].toString()));
				expenseList.add(expense);
			}
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		return expenseList;
	}
	
	public List<ExpenseLastMonthByCategory> getExpenseLastMonthByCategory(int userId){
		List returnList = null;
		List<ExpenseLastMonthByCategory> expenseList = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String my_query = "Select sum(tx.amount), month(tx.expenseDate), year(tx.expenseDate), cat.name \r\n"
					+ "from UserTransactionBase tx, Category cat, Subcategory subcat \r\n"+
					" where tx.userId ="+userId+" and cat.categoryId =  subcat.categoryId and subcat.subcategoryId = tx.subcategoryId \r\n"+
					" group by month(tx.expenseDate), year(tx.expenseDate), cat.name \r\n"+ 
					" order by year(tx.expenseDate) desc, month(tx.expenseDate) desc";
						     
			Query query = session.createQuery(my_query);
			returnList  = query.list();
			transaction.commit();
			
			//Get results and put them into expense data model objects
			expenseList  = new ArrayList();
			for(Iterator i = returnList.iterator();i.hasNext();) {
				Object objects [] = (Object []) i.next();
				ExpenseLastMonthByCategory expense = new ExpenseLastMonthByCategory();
				expense.setAmount(Double.parseDouble(objects[0].toString()));
				expense.setMonth(Integer.parseInt(objects[1].toString()));
				expense.setYear(Integer.parseInt(objects[2].toString()));
				expense.setCategoryName(objects[3].toString());
				expenseList.add(expense);
			}
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		return expenseList;
	}
	
	
	public List<ExpenseLastMonthByPayment> getExpenseLastMonthByPayment(int userId){
		List returnList = null;
		List<ExpenseLastMonthByPayment> expenseList = null;
		Session session = factory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			String my_query = "Select sum(tx.amount), month(tx.expenseDate), year(tx.expenseDate), pay.name, pay.bankName  \r\n"
					+ "from UserTransactionBase tx, PaymentType pay\r\n"+
					" where tx.userId ="+userId+" and tx.paymentTypeId = pay.paymentTypeID \r\n"+
					" group by month(tx.expenseDate), year(tx.expenseDate), pay.paymentTypeID\r\n"+ 
					" order by year(tx.expenseDate) desc, month(tx.expenseDate) desc";
						     
			Query query = session.createQuery(my_query);
			returnList  = query.list();
			transaction.commit();
			
			//Get results and put them into expense data model objects
			expenseList  = new ArrayList();
			for(Iterator i = returnList.iterator();i.hasNext();) {
				Object objects [] = (Object []) i.next();
				ExpenseLastMonthByPayment expense = new ExpenseLastMonthByPayment();
				expense.setAmount(Double.parseDouble(objects[0].toString()));
				expense.setMonth(Integer.parseInt(objects[1].toString()));
				expense.setYear(Integer.parseInt(objects[2].toString()));
				expense.setPaymentName(objects[3].toString());
				expense.setBankName(objects[4].toString());
				expenseList.add(expense);
			}
			
		}catch(HibernateException ex) {
			if (transaction != null)
					transaction.rollback();
			ex.printStackTrace();
		}finally {
			session.close();
		}
		return expenseList;
	}
	public static void main(String [] args) {
		ReportManagement rm = new ReportManagement();
		/*List list = rm.getExpenseByMonth(1, 1, 2018, 5);
		for(Iterator i =list.iterator();i.hasNext();) {
			ExpenseByMonth exp = (ExpenseByMonth)i.next();
			System.out.println("Month:"+exp.getMonth());
			System.out.println("Amount:"+exp.getAmount());
			System.out.println("Year:"+exp.getYear());
			System.out.println("Username:"+exp.getUserName());
		}
		*/
		
		
		List list = rm.getExpenseLastMonthByPayment(1);
		for(Iterator i =list.iterator();i.hasNext();) {
			ExpenseLastMonthByPayment exp = (ExpenseLastMonthByPayment)i.next();
			System.out.println("Month:"+exp.getMonth());
			System.out.println("Amount:"+exp.getAmount());
			System.out.println("Year:"+exp.getYear());
			System.out.println("Pay name:"+exp.getPaymentName());
			System.out.println("Pay name:"+exp.getBankName());
		}	
	}
}
