package com.customer.rewardsprogram.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.customer.rewardsprogram.dto.RewardSummaryDTO;
import com.customer.rewardsprogram.model.Transaction;
import com.customer.rewardsprogram.service.RewardService;

@RestController
@RequestMapping("/transactions")
public class RewardsController {

	@Autowired
	private RewardService rewardService;

	@GetMapping
	public List<Transaction> getTransactions(
			@RequestParam(required = false) Long customerId,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
			) {
		if (month != null) {
			LocalDate startDate = month.atDay(1);
			LocalDate endDate = month.atEndOfMonth();
			return rewardService.getCustomerTransactionsByDateRange(customerId, startDate, endDate);
		}
		if (customerId != null) {
			return rewardService.getCustomerTransactions(customerId);
		}
		return rewardService.getLastThreeMonthsTransactions();
	}

	@GetMapping("/rewards/{customerId}")
	public RewardSummaryDTO getRewardSummary(@PathVariable Long customerId) {
		return rewardService.calculateRewards(customerId);
	}
}
