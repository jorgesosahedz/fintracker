package com.avantplus.fintracker.data.models;

public class ExpenseByMonth {
	private int Month;
	private int Year;
	private double Amount;
	private String UserName;
	private int Periods;
	
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		this.Month = month;
	}
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		this.Year = year;
	}
	public double getAmount() {
		return Amount;
	}
	public void setAmount(double amount) {
		this.Amount = amount;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		this.UserName = userName;
	}
	public int getPeriods() {
		return Periods;
	}
	public void setPeriods(int periods) {
		this.Periods = periods;
	}
	
}
