package com.avantplus.fintracker.data.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_type")
public class PaymentType {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int paymentTypeID;
	
	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name="bank_name")
	private String bankName;
	
	@Column(name="cut_date")
	private Date cutDate;
	
	@Column(name="payment_date_limit" )
	private Date payDateLimit;
	
	@Column(name="amount_limit")
	private Double amountLimit;
	
	@Column(name="credit_interest_rate_month")
	private Double creditMonthlyInterestRate;
	
	@Column(name="is_deleted")
	private int isDeleted;
	
	public int getPaymentTypeID() {
		return paymentTypeID;
	}
	public void setPaymentTypeID(int paymentTypeID) {
		this.paymentTypeID = paymentTypeID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public Date getCutDate() {
		return cutDate;
	}
	public void setCutDate(Date cutDate) {
		this.cutDate = cutDate;
	}
	public Date getPayDateLimit() {
		return payDateLimit;
	}
	public void setPayDateLimit(Date payDateLimit) {
		this.payDateLimit = payDateLimit;
	}
	public Double getAmountLimit() {
		return amountLimit;
	}
	public void setAmountLimit(Double amountLimit) {
		this.amountLimit = amountLimit;
	}
	public Double getCreditMonthlyInterestRate() {
		return creditMonthlyInterestRate;
	}
	public void setCreditMonthlyInterestRate(Double creditMonthlyInterestRate) {
		this.creditMonthlyInterestRate = creditMonthlyInterestRate;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	

}
