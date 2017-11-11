package com.avantplus.fintracker.data.entity;

import java.sql.Timestamp;

import javax.persistence.*;

@Entity
@Table (name="transaction")
public class UserTransactionBase {
	@Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int transactionId;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="subcategory_id")
	private int subcategoryId;
	
	@Column(name="payment_type_id")
	private int paymentTypeId;
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name="timestamp")
	private Timestamp timeStamp;
	
	@Column(name="description")
	private String description;
	
	@Column(name="is_deleted")
	private int isDeleted;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSubCategoryId() {
		return subcategoryId;
	}
	public void setSubCategoryId(int subCategoryId) {
		this.subcategoryId = subCategoryId;
	}
	public int getPaymentTypeId() {
		return paymentTypeId;
	}
	public void setPaymentTypeId(int paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionID) {
		this.transactionId = transactionID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSubcategoryId() {
		return subcategoryId;
	}
	public void setSubcategoryId(int subcategoryId) {
		this.subcategoryId = subcategoryId;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
