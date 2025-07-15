package com.customer.rewardsprogram.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "CustomerId cannot be null")
	private Long customerId;
	
	@NotNull(message = "Amount cannot be null")
	@Positive(message = "Amount must be greater than zero")
	private Double amount;
	
	@NotNull(message = "Transaction Date cannot be null")
	@PastOrPresent(message = "Transaction cannot be in future")
	private LocalDate transactionDate;
	
	public Transaction() {
		super();
	}
	public Transaction(Long id, Long customerId, LocalDate transactionDate,Double amount) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
