package com.avantplus.fintracker.services;

import java.util.Iterator;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.avantplus.fintracker.data.entity.Category;
import com.avantplus.fintracker.data.entity.PaymentType;
import com.avantplus.fintracker.data.entity.Subcategory;
import com.avantplus.fintracker.data.entity.User;
import com.avantplus.fintracker.data.entity.UserTransaction;
import com.avantplus.fintracker.data.entity.UserTransactionBase;
import com.avantplus.fintracker.data.management.CategoryManagement;
import com.avantplus.fintracker.data.management.PaymentTypeManagement;
import com.avantplus.fintracker.data.management.SubcategoryManagement;
import com.avantplus.fintracker.data.management.TransactionManagement;
import com.avantplus.fintracker.data.management.UserManagement;

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
	public <T> void addTransaction(List<UserTransaction> userTransactionList) {
		UserManagement um = new UserManagement();
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
			utBase.setAmount(userTransaction.getAmount());
			
			//Store transaction into Database			
			utm.addTransaction(utBase);
		}
	}
	
	@POST
	@Path("/transaction/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public <T> void updateTransaction(List<UserTransaction> userTransactionList) {
		UserManagement um = new UserManagement();
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
			utBase.setTransactionId(userTransaction.getTransactionId());
			utBase.setSubCategoryId(userTransaction.getSubCategoryId());
			utBase.setPaymentTypeId(userTransaction.getPaymentTypeId());
			utBase.setDescription(userTransaction.getDescription());
			utBase.setAmount(userTransaction.getAmount());
			
			//Store transaction into Database			
			utm.updateTransaction(utBase);
			
		}
	}
	
	@POST
	@Path("/transaction/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public <T> void deleteTransaction(UserTransactionBase userTransactionBase) {
		TransactionManagement utm = new TransactionManagement();
		
		//Store transaction into Database			
		utm.deleteTransaction(userTransactionBase);

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
	public void updateCategory(Category category) {
		CategoryManagement cm = new CategoryManagement();
		cm.updateCategory(category);
	}
	
	@POST
	@Path("/category/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addCategory(Category category) {
		CategoryManagement cm = new CategoryManagement();
		cm.addCategory(category);
	}
	
	@GET
	@Path("/category/delete/{categoryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteCategory(@PathParam("categoryId") int categoryId) {
		CategoryManagement cm = new CategoryManagement();
		cm.deleteCategory(categoryId);
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
	public void addPayment(PaymentType paymentType) {
		PaymentTypeManagement pt = new PaymentTypeManagement();
		pt.addPayment(paymentType);
	}
	
	@POST
	@Path("/paymenttypes/update/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updatePaymentType(PaymentType paymentType) {
		PaymentTypeManagement pt = new PaymentTypeManagement();
		pt.updatePaymentType(paymentType);
	}
	
	@GET
	@Path("/paymenttypes/delete/{paymentTypeId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deletePaymentType(@PathParam("paymentTypeId") int paymentTypeId) {
		PaymentTypeManagement pt = new PaymentTypeManagement();
		pt.deletePaymentType(paymentTypeId);
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
	
	@POST
	@Path("/subcategory/add/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void addSubcategories(Subcategory subcategory) {
		SubcategoryManagement cm = new SubcategoryManagement();
		cm.addSubcategory(subcategory); 
	}
	
	@POST
	@Path("/subcategory/update/")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateSubcategories(Subcategory subcategory) {
		SubcategoryManagement cm = new SubcategoryManagement();
		cm.updateSubcategory(subcategory); 
	}
	
	@GET
	@Path("/subcategory/delete/{subcategoryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteSubcategory(@PathParam("subcategoryId") int subcategoryId) {
		SubcategoryManagement cm = new SubcategoryManagement();
		cm.deleteSubcategory(subcategoryId);
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
