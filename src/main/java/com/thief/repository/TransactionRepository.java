package com.thief.repository;

import com.thief.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByAmountGreaterThan(Double amount);
}
