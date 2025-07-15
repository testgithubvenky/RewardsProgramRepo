package com.customer.rewardsprogram.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.customer.rewardsprogram.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	List<Transaction> findByTransactionDateAfter(LocalDate date);
	List<Transaction> findByCustomerId(Long customerId);
	List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate start, LocalDate end);
}
