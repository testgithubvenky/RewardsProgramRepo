package com.customer.rewardsprogram.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.customer.rewardsprogram.dto.RewardSummaryDTO;
import com.customer.rewardsprogram.exception.ResourceNotFoundException;
import com.customer.rewardsprogram.model.Transaction;
import com.customer.rewardsprogram.repository.TransactionRepository;

@Service
public class RewardService {
	@Autowired
	private TransactionRepository transactionRepository;

	public List<Transaction> getLastThreeMonthsTransactions(){
		LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
		return getCustomerTransactionsByDateRange(null, threeMonthsAgo, LocalDate.now());
	}

	public List<Transaction> getCustomerTransactions(Long customerId){
		List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
		if(transactions.isEmpty() || null == transactions) {
			throw new ResourceNotFoundException("No Transactions found for customerId :" + customerId);
		}
		return transactions;
	}

	public List<Transaction> getCustomerTransactionsByMonth(Long customerId, YearMonth month){
		LocalDate startDate = month.atDay(1);
		LocalDate endDate = month.atEndOfMonth();
		return getCustomerTransactionsByDateRange(customerId, startDate, endDate);
	}

	public List<Transaction> getCustomerTransactionsByDateRange(Long customerId, LocalDate startDate, LocalDate endDate) {
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException("Start date " + startDate + " is after end date " + endDate);
		}
		List<Transaction> transactions;
		if (customerId != null) {
			transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);
		} else {
			transactions = transactionRepository.findByTransactionDateAfter(startDate);
		}
		if (transactions == null || transactions.isEmpty()) {
			String message = customerId == null
					? "No Transactions found in last three months"
							: "No Transactions found for customerId: " + customerId + " between " + startDate + " and " + endDate;
			throw new ResourceNotFoundException(message);
		}
		return transactions;
	}

	public RewardSummaryDTO calculateRewards(Long customerId) {
		List<Transaction> transactions = getCustomerTransactions(customerId);
		Map<String, Integer> montlyPoints = new HashMap<>();
		int totalPoints=0;
		for(Transaction transaction : transactions) {
			int points = calculatePoints(transaction.getAmount());
			String month = transaction.getTransactionDate().getMonth().toString();
			montlyPoints.put(month, montlyPoints.getOrDefault(month, 0) + points);
			totalPoints += points;
		}
		return new RewardSummaryDTO(customerId, montlyPoints, totalPoints, transactions);
	}

	public static int calculatePoints(double amount) {
		int points = 0;
		if(amount > 100) {
			points += (int)Math.floor((amount-100)*2);
			points += 50;
		}else if(amount > 50) {
			points += (int)Math.floor(amount-50);
		}
		return points;
	}
}
