package com.customer.rewardsprogram.controllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import com.customer.rewardsprogram.controller.RewardsController;
import com.customer.rewardsprogram.dto.RewardSummaryDTO;
import com.customer.rewardsprogram.exception.ResourceNotFoundException;
import com.customer.rewardsprogram.model.Transaction;
import com.customer.rewardsprogram.service.RewardService;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RewardService rewardService;

	@Test
	void getRecentTransactions_success() throws Exception {
		List<Transaction> txns = List.of(new Transaction(1L, 1001L, LocalDate.now(), 120.0));
		when(rewardService.getLastThreeMonthsTransactions()).thenReturn(txns);
		mockMvc.perform(get("/transactions/recent"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].amount").value(120.0));
	}

	@Test
	void getCustomerTransactions_withoutMonth_success() throws Exception {
		List<Transaction> txns = List.of(new Transaction(2L, 1002L, LocalDate.now(), 95.0));
		when(rewardService.getCustomerTransactions(1002L)).thenReturn(txns);
		mockMvc.perform(get("/transactions/customer/1002"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].customerId").value(1002));
	}

	@Test
	void getCustomerTransactions_withMonth_success() throws Exception {
		YearMonth ym = YearMonth.of(2025, 7);
		List<Transaction> txns = List.of(new Transaction(3L, 1003L, ym.atDay(10), 75.0));
		when(rewardService.getCustomerTransactionsByMonth(1003L, ym)).thenReturn(txns);
		mockMvc.perform(get("/transactions/customer/1003")
				.param("month", "2025-07"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].amount").value(75.0));
	}

	@Test
	void getRewardSummary_success() throws Exception {
		List<Transaction> txns = List.of(new Transaction(4L, 1004L, LocalDate.now(), 150.0));
		RewardSummaryDTO summary = new RewardSummaryDTO(1004L, Map.of("JULY", 150), 150, txns);
		when(rewardService.calculateRewards(1004L)).thenReturn(summary);
		mockMvc.perform(get("/transactions/rewards/1004"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.totalPoints").value(150))
		.andExpect(jsonPath("$.transactions[0].amount").value(150.0));
	}

	@Test
	void getCustomerTransactions_notFound_returns404() throws Exception {
		when(rewardService.getCustomerTransactions(888L)).thenThrow(new ResourceNotFoundException("No transactions"));
		mockMvc.perform(get("/transactions/customer/888"))
		.andExpect(status().isNotFound());
	}

	@Test
	void getRewardSummary_notFound_returns404() throws Exception {
		when(rewardService.calculateRewards(777L)).thenThrow(new ResourceNotFoundException("Not found"));
		mockMvc.perform(get("/transactions/rewards/777"))
		.andExpect(status().isNotFound());
	}

	@Test
	void getCustomerTransactions_invalidMonthFormat_returns400() throws Exception {
		mockMvc.perform(get("/transactions/customer/1001")
				.param("month", "Jul-25")) // Invalid format for @DateTimeFormat
		.andExpect(status().isBadRequest());
	}
}
