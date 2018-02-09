package com.avantplus.fintracker.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.hibernate.exception.ConstraintViolationException;

import com.avantplus.fintracker.data.entity.Category;
import com.avantplus.fintracker.data.entity.PaymentType;
import com.avantplus.fintracker.data.entity.Subcategory;
import com.avantplus.fintracker.data.entity.User;
import com.avantplus.fintracker.data.entity.UserTransaction;
import com.avantplus.fintracker.data.entity.UserTransactionBase;
import com.avantplus.fintracker.data.management.CategoryManagement;
import com.avantplus.fintracker.data.management.PaymentTypeManagement;
import com.avantplus.fintracker.data.management.ReportManagement;
import com.avantplus.fintracker.data.management.SubcategoryManagement;
import com.avantplus.fintracker.data.management.TransactionManagement;
import com.avantplus.fintracker.data.management.UserManagement;
import com.avantplus.fintracker.data.models.EntityId;
import com.avantplus.fintracker.data.models.ExpenseByMonth;
import com.avantplus.fintracker.data.models.ExpenseLastMonthByCategory;
import com.avantplus.fintracker.data.models.ExpenseLastMonthByPayment;
import com.avantplus.fintracker.utils.FintrackerConstants;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

@Path("/fintrackerservices")
public class FintrackerService {
	//--- TRANSACTIONS
	@GET
	@Path("/transaction/list/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UserTransactionBase> getTransactions(@PathParam("userName") String userName){
		TransactionManagement tm = new TransactionManagement();
		UserManagement um = new UserManagement();
		User user = um.getUserByUsername(userName);
		int userId = user.getUserId();
		List<UserTransactionBase> transactionList = tm.getTransactions(userId);
		return transactionList;
	}
	
	@POST
	@Path("/transaction/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<UserTransaction> addTransaction(List<UserTransaction> userTransactionList) {
		UserManagement um = new UserManagement();
		List<UserTransaction> returnTransactionList = new ArrayList<UserTransaction>();
		TransactionManagement utm = new TransactionManagement();
		
		for(Iterator<UserTransaction> iterator = userTransactionList.iterator();iterator.hasNext();) {
			UserTransaction userTransaction =  iterator.next();
			
			//Get user Id base on username
			User user = um.getUserByUsername(userTransaction.getUserName());
			
			//Set userid to user transaction 
			userTransaction.setUserId(user.getUserId());
			
			UserTransactionBase utBase = new UserTransactionBase();
			utBase.setUserId(userTransaction.getUserId());
			utBase.setTransactionId(userTransaction.getTransactionId());
			utBase.setSubCategoryId(userTransaction.getSubCategoryId());
			utBase.setPaymentTypeId(userTransaction.getPaymentTypeId());
			utBase.setDescription(userTransaction.getDescription());
			utBase.setExpenseDate(userTransaction.getExpenseDate());
			utBase.setAmount(userTransaction.getAmount());
			
			//Store transaction into Database			
			int transactionId = utm.addTransaction(utBase);
			UserTransaction utb = utm.getTransactionsById(transactionId);
			returnTransactionList.add(utb);
		}
		return returnTransactionList;
	}
	
	@POST
	@Path("/transaction/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<UserTransaction> updateTransaction(List<UserTransaction> userTransactionList) {
		UserManagement um = new UserManagement();
		TransactionManagement utm = new TransactionManagement();
		List<UserTransaction> returnTransactionList = new ArrayList<UserTransaction>();
		
		for(Iterator<UserTransaction> iterator = userTransactionList.iterator();iterator.hasNext();) {
			UserTransaction userTransaction =  iterator.next();
			
			//Get user Id base on username
			User user = um.getUserByUsername(userTransaction.getUserName());
			
			//Set userid to user transaction 
			userTransaction.setUserId(user.getUserId());
			
			UserTransactionBase utBase = new UserTransactionBase();
			utBase.setUserId(userTransaction.getUserId());
			utBase.setTransactionId(userTransaction.getTransactionId());
			utBase.setTransactionId(userTransaction.getTransactionId());
			utBase.setSubCategoryId(userTransaction.getSubCategoryId());
			utBase.setPaymentTypeId(userTransaction.getPaymentTypeId());
			utBase.setDescription(userTransaction.getDescription());
			utBase.setExpenseDate(userTransaction.getExpenseDate());
			utBase.setAmount(userTransaction.getAmount());
			
			//Update transaction into Database			
			utm.updateTransaction(utBase);
			
			UserTransaction utb = utm.getTransactionsById(utBase.getTransactionId());
			returnTransactionList.add(utb);
			
		}
		return returnTransactionList;
	}
	
	@POST
	@Path("/transaction/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public int deleteTransaction(List<EntityId> transactionIds) {
		TransactionManagement utm = new TransactionManagement();
		int deleted_entities = 0;
		for (Iterator<EntityId> i = transactionIds.iterator();i.hasNext(); ) {
			EntityId entity = i.next();
			deleted_entities += utm.deleteTransaction(entity.getId());
		}
		return deleted_entities;

	}
	
	//---CATEGORIES
	@GET
	@Path("/category/list/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategories(@PathParam("userName") String userName) {
		CategoryManagement cm = new CategoryManagement();
		UserManagement um = new UserManagement();
		User user = um.getUserByUsername(userName);
		int userId = user.getUserId();
		List<Category> categoryList = (List<Category>)cm.getCategoriesbyUserId(userId); 
		
		return categoryList;
	}
	
	@GET
	@Path("/category/list/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Category> getCategories() {
		CategoryManagement cm = new CategoryManagement();
		UserManagement um = new UserManagement();
		List<Category> categoryList = (List<Category>)cm.getCategories(); 
		
		return categoryList;
	}
	
	@POST
	@Path("/category/update/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Category> updateCategory(List<Category> categoryList) {
		CategoryManagement cm = new CategoryManagement();
		
		List<Category> updatedCategoryList = new ArrayList<Category>();
		for (Iterator<Category> i = categoryList.iterator(); i.hasNext();) {
			Category category = i.next();
			try {
				
				cm.updateCategory(category);
			
			}catch(PersistenceException e) {
		    	//Insert error code in category instance
				category.setCategoryId(FintrackerConstants.CATEGORY_DUPLICATE_ERR_CODE);
			}
			
			updatedCategoryList.add(category);
		}
		return updatedCategoryList;
	}
	
	@POST
	@Path("/category/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Category> addCategory(List<Category> categoryList) {
		CategoryManagement cm = new CategoryManagement();
		
		List<Category> updatedCategoryList = new ArrayList<Category>();
		for (Iterator<Category> i = categoryList.iterator(); i.hasNext();) {
			Category category = i.next();
			try {
				
				cm.addCategory(category);
			}
			catch(Exception e) {
				//Insert error code in category instance
				category.setCategoryId(FintrackerConstants.CATEGORY_DUPLICATE_ERR_CODE);
			}
			
			updatedCategoryList.add(category);
		}
		
	return updatedCategoryList;
	}
	
	@POST
	@Path("/category/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	public int deleteCategory(List<EntityId> categoryIds) {
		CategoryManagement cm = new CategoryManagement();
		int deleted_categories = 0;
		for (Iterator<EntityId> i = categoryIds.iterator();i.hasNext(); ) {
			EntityId catId = i.next();
			deleted_categories += cm.deleteCategory(catId.getId());	
		}
		return deleted_categories;
	}
	
	//---PAYMENT TYPES
	@GET
	@Path("/paymenttypes/list/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PaymentType> getPaymentTypes(@PathParam("userName") String userName) {
		PaymentTypeManagement cm = new PaymentTypeManagement();
		UserManagement um = new UserManagement();
		User user = um.getUserByUsername(userName);
		int userId = user.getUserId();
		List<PaymentType> paymentTypeList = (List<PaymentType>)cm.getPaymentTypesByUserId(userId); 
		
		return paymentTypeList;
	}
	
	@GET
	@Path("/paymenttypes/list/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PaymentType> getPaymentTypes() {
		PaymentTypeManagement cm = new PaymentTypeManagement();
		List<PaymentType> paymentTypeList = (List<PaymentType>)cm.getPaymentTypes(); 
		
		return paymentTypeList;
	}
	
	@POST
	@Path("/paymenttypes/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<PaymentType> addPayment(List<PaymentType> paymentTypeList) {
		PaymentTypeManagement pt = new PaymentTypeManagement();
		
		List<PaymentType> updatedPaymentList = new ArrayList<PaymentType>();
		for (Iterator<PaymentType> i = paymentTypeList.iterator(); i.hasNext();) {
		
			PaymentType payment = i.next();
			try {
			
				pt.addPayment(payment);
			
			}catch(Exception e) {
				//Insert error code in category instance
				payment.setPaymentTypeID(FintrackerConstants.PAYMENT_DUPLICATE_ERR_CODE);
			}
			
			updatedPaymentList.add(payment);
		}
		
		
		return updatedPaymentList;
	}
	
	@POST
	@Path("/paymenttypes/update/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<PaymentType> updatePaymentType(List<PaymentType> paymentTypeList) {
		PaymentTypeManagement pt = new PaymentTypeManagement();
		
		List<PaymentType> updatedPaymentList = new ArrayList<PaymentType>();
		for (Iterator<PaymentType> i = paymentTypeList.iterator(); i.hasNext();) {
		
			PaymentType payment = i.next();
			try {
			
				pt.updatePaymentType(payment);
			
			}catch(PersistenceException e) {
				
		    	//Insert error code in category instance
				payment.setPaymentTypeID(FintrackerConstants.PAYMENT_DUPLICATE_ERR_CODE);
			}
			
			updatedPaymentList.add(payment);
		}
		
		return updatedPaymentList;
	}
	
	@POST
	@Path("/paymenttypes/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	public int deletePaymentType(List<EntityId>  paymentTypeIds) {
		PaymentTypeManagement pt = new PaymentTypeManagement();
		int deleted_payments = 0;
		for (Iterator<EntityId> i = paymentTypeIds.iterator();i.hasNext(); ) {
			EntityId entity = i.next();
			deleted_payments += pt.deletePaymentType(entity.getId());
		}
		
		return deleted_payments;
	}
	
	//UPDATE SUBCATEGORIES
	
	@GET
	@Path("/subcategory/list/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subcategory> getSubategories() {
		SubcategoryManagement scm = new SubcategoryManagement();
		List<Subcategory> subcategoryList = (List<Subcategory>)scm.getSubcategories(); 

		return subcategoryList;
	}
	
	@GET
	@Path("/subcategory/list/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subcategory> getSubcategories(@PathParam("userName") String userName) {
		SubcategoryManagement cm = new SubcategoryManagement();
		UserManagement um = new UserManagement();
		User user = um.getUserByUsername(userName);
		int userId = user.getUserId();
		List<Subcategory> categoryList = (List<Subcategory>)cm.getSubcategoriesByUserId(userId); 
		
		return categoryList;
	}
	@GET
	@Path("/subcategory/list/id/{subcategoryId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Subcategory getSubcategoryById(@PathParam("subcategoryId") int subcategoryId) {
		SubcategoryManagement cm = new SubcategoryManagement();
		
		Subcategory subcategory  = new Subcategory();
		subcategory = (Subcategory)cm.getSubcategoryById(subcategoryId); 
		
		return subcategory;
	}
	
	@POST
	@Path("/subcategory/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Subcategory> addSubcategories(List<Subcategory> subcategoryList) {
		SubcategoryManagement cm = new SubcategoryManagement();

		List<Subcategory> updatedSubcategoryList = new ArrayList<Subcategory>();
		for (Iterator<Subcategory> i = subcategoryList.iterator(); i.hasNext();) {
			Subcategory subcategory = i.next();
		
			try {
			
				cm.addSubcategory(subcategory);
			
			}catch(Exception e) {
			
				//Insert error code in category instance
				subcategory.setSubcategoryId(FintrackerConstants.SUBCATEGORY_DUPLICATE_ERR_CODE);
			}
			
			updatedSubcategoryList.add(subcategory);
		}
		return updatedSubcategoryList;
	}
	
	@POST
	@Path("/subcategory/update/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Subcategory> updateSubcategories(List<Subcategory> subcategoryList) {
		SubcategoryManagement cm = new SubcategoryManagement();
		
		List<Subcategory> updatedSubcategoryList = new ArrayList<Subcategory>();
		
		for (Iterator<Subcategory> i = subcategoryList.iterator(); i.hasNext();) {
			Subcategory subcategory = i.next();	
			
			try {
				
				cm.updateSubcategory(subcategory);
				
			}catch(PersistenceException e) {
				
				//Insert error code in category instance
				subcategory.setCategoryId(FintrackerConstants.SUBCATEGORY_DUPLICATE_ERR_CODE);
			}
			
			updatedSubcategoryList.add(subcategory);
		}
	 return updatedSubcategoryList;	 
	}
	
	@POST
	@Path("/subcategory/delete/")
	@Consumes(MediaType.APPLICATION_JSON)
	public int deleteSubcategory(List<EntityId> subcategoryIds) {
		SubcategoryManagement cm = new SubcategoryManagement();
		int deleted_subcats = 0;
		for (Iterator<EntityId> i = subcategoryIds.iterator();i.hasNext(); ) {
			EntityId entity = i.next();
			deleted_subcats += cm.deleteSubcategory(entity.getId());	
		}
		
		return deleted_subcats;
	}
	
	@POST
	@Path("/report/expensebymonth/")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ExpenseByMonth> getExpenseByMonth(ExpenseByMonth expense) {
		
		ReportManagement cm = new ReportManagement();
		UserManagement um = new UserManagement();
		
		User user = um.getUserByUsername(expense.getUserName());
		
		int userId = user.getUserId();
		
		List<ExpenseByMonth> expenseList = (List<ExpenseByMonth>)cm.getExpenseByMonth(userId, expense.getMonth(), expense.getYear(), expense.getPeriods()); 
		
		return expenseList;
	}
	
	@GET
	@Path("/report/expenselastmonthbycategory/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ExpenseLastMonthByCategory> getExpenseLastMonthByCategory(@PathParam("userName") String userName) {
		
		ReportManagement cm = new ReportManagement();
		UserManagement um = new UserManagement();
		
		User user = um.getUserByUsername(userName);
		
		int userId = user.getUserId();
		
		List<ExpenseLastMonthByCategory> expenseList = (List<ExpenseLastMonthByCategory>)cm.getExpenseLastMonthByCategory(userId); 
		
		return expenseList;
	}
	
	@GET
	@Path("/report/expenselastmonthbypayment/{userName}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ExpenseLastMonthByPayment> getExpenseLastMonthByPayment(@PathParam("userName") String userName) {
		
		ReportManagement cm = new ReportManagement();
		UserManagement um = new UserManagement();
		
		User user = um.getUserByUsername(userName);
		
		int userId = user.getUserId();
		
		List<ExpenseLastMonthByPayment> expenseList = (List<ExpenseLastMonthByPayment>)cm.getExpenseLastMonthByPayment(userId); 
		
		return expenseList;
	}
	
	public static void main(String [] args) {
		FintrackerService serv =  new FintrackerService();
		List<Category> list = serv.getCategories("");
		for (Iterator<Category> i = list.iterator();i.hasNext();) {
			Category cat = (Category)i.next();
			System.out.println("Cat name:"+cat.getName());
		}
		
	}
}
