package com.customer.rewardsprogram.controllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.customer.rewardsprogram.controller.RewardsController;
import com.customer.rewardsprogram.dto.RewardSummaryDTO;
import com.customer.rewardsprogram.model.Transaction;
import com.customer.rewardsprogram.service.RewardService;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RewardService rewardService;

	@Test
	void testGetTransactions_recentThreeMonths() throws Exception {
		List<Transaction> transactions = List.of(new Transaction(1L, 101L, LocalDate.now().minusDays(10), 120.0));
		when(rewardService.getLastThreeMonthsTransactions()).thenReturn(transactions);
		mockMvc.perform(get("/transactions"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].customerId").value(101))
		.andExpect(jsonPath("$[0].amount").value(120.0));
	}

	@Test
	void testGetTransactions_byCustomerId_only() throws Exception {
		List<Transaction> transactions = List.of(new Transaction(2L, 102L, LocalDate.now(), 85.0));
		when(rewardService.getCustomerTransactions(102L)).thenReturn(transactions);
		mockMvc.perform(get("/transactions?customerId=102"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].customerId").value(102));
	}

	@Test
	void testGetTransactions_byCustomerId_and_month() throws Exception {
		List<Transaction> transactions = List.of(new Transaction(3L, 103L, LocalDate.of(2025, 7, 12), 90.0));
		LocalDate start = LocalDate.of(2025, 7, 1);
		LocalDate end = LocalDate.of(2025, 7, 31);
		when(rewardService.getCustomerTransactionsByDateRange(103L, start, end)).thenReturn(transactions);
		mockMvc.perform(get("/transactions?customerId=103&month=2025-07"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].customerId").value(103))
		.andExpect(jsonPath("$[0].amount").value(90.0));
	}

	@Test
	void testGetRewardSummary_success() throws Exception {
		RewardSummaryDTO summary = new RewardSummaryDTO(104L, Map.of("JULY", 100), 100, List.of());
		when(rewardService.calculateRewards(104L)).thenReturn(summary);
		mockMvc.perform(get("/transactions/rewards/104"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.customerId").value(104))
		.andExpect(jsonPath("$.totalPoints").value(100));
	}
}
