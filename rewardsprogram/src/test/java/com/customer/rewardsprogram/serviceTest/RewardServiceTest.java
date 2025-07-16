package com.customer.rewardsprogram.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.customer.rewardsprogram.dto.RewardSummaryDTO;
import com.customer.rewardsprogram.exception.ResourceNotFoundException;
import com.customer.rewardsprogram.model.Transaction;
import com.customer.rewardsprogram.repository.TransactionRepository;
import com.customer.rewardsprogram.service.RewardService;

public class RewardServiceTest {
	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private RewardService rewardService;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetLastThreeMonthsTransactions_success() {
		List<Transaction> mockTxns = List.of(new Transaction(1L, 1001L, LocalDate.now().minusDays(10), 120.0));
		when(transactionRepository.findByTransactionDateAfter(any())).thenReturn(mockTxns);
		List<Transaction> result = rewardService.getLastThreeMonthsTransactions();
		assertEquals(1, result.size());
	}

	@Test
	void testGetLastThreeMonthsTransactions_empty() {
		when(transactionRepository.findByTransactionDateAfter(any())).thenReturn(List.of());
		ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> rewardService.getLastThreeMonthsTransactions());
		assertTrue(ex.getMessage().contains("No Transactions"));
	}

	@Test
	void testGetCustomerTransactions_success() {
		List<Transaction> mockTxns = List.of(new Transaction(2L, 1002L, LocalDate.now(), 75.0));
		when(transactionRepository.findByCustomerId(1002L)).thenReturn(mockTxns);
		List<Transaction> result = rewardService.getCustomerTransactions(1002L);
		assertEquals(75.0, result.get(0).getAmount());
	}

	@Test
	void testGetCustomerTransactions_empty() {
		when(transactionRepository.findByCustomerId(9999L)).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> rewardService.getCustomerTransactions(9999L));
	}

	@Test
	void testGetCustomerTransactionsByMonth_success() {
		YearMonth month = YearMonth.now();
		LocalDate startDate = month.atDay(1);
		LocalDate endDate = month.atEndOfMonth();
		List<Transaction> mockTxns = List.of(new Transaction(3L, 1003L, startDate.plusDays(5), 100.0));
		when(transactionRepository.findByCustomerIdAndTransactionDateBetween(anyLong(), any(), any())).thenReturn(mockTxns);
		List<Transaction> result = rewardService.getCustomerTransactionsByMonth(1003L, month);
		assertEquals(100.0, result.get(0).getAmount());
	}

	@Test
	void testGetCustomerTransactionsByDateRange_invalidRange() {
		LocalDate start = LocalDate.of(2025, 1, 31);
		LocalDate end = LocalDate.of(2025, 1, 1);
		assertThrows(IllegalArgumentException.class, () -> rewardService.getCustomerTransactionsByDateRange(1004L, start, end));
	}

	@Test
	void testCalculatePoints_below50() {
		assertEquals(0, RewardService.calculatePoints(45.0));
	}

	@Test
	void testCalculatePoints_between50And100() {
		assertEquals(25, RewardService.calculatePoints(75.0));
	}

	@Test
	void testCalculatePoints_above100() {
		assertEquals(90, RewardService.calculatePoints(120.0)); // 20*2 + 50 = 90
	}

	@Test
	void testCalculateRewards_success() {
		List<Transaction> mockTxns = List.of(new Transaction(4L, 1005L, LocalDate.of(2025, 7, 10), 120.0),new Transaction(5L, 1005L, LocalDate.of(2025, 7, 15), 90.0));
		when(transactionRepository.findByCustomerId(1005L)).thenReturn(mockTxns);
		RewardSummaryDTO summary = rewardService.calculateRewards(1005L);
		assertEquals(2, summary.getTransactions().size());
		assertEquals(130, summary.getTotalPoints()); // 90 from first, 25 from second
		assertEquals(130, summary.getMonthlyPoints().get("JULY"));
	}

	@Test
	void testCalculateRewards_emptyTransactions() {
		when(transactionRepository.findByCustomerId(1010L)).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class,() -> rewardService.calculateRewards(1010L));
	}
}
