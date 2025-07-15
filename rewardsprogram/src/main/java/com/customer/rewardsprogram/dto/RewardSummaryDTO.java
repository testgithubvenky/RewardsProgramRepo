package com.customer.rewardsprogram.dto;

import java.util.List;
import java.util.Map;
import com.customer.rewardsprogram.model.Transaction;

public class RewardSummaryDTO {	
	private Long customerId;
	private Map<String, Integer> monthlyPoints;
	private int totalPoints;
	private List<Transaction> transactions;
	public RewardSummaryDTO(Long customerId, Map<String, Integer> monthlyPoints, int totalPoints, List<Transaction> transactions) {
		super();
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
		this.transactions = transactions;
	}	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}
	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public List<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
}
