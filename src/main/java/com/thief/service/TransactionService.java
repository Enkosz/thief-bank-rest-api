package com.thief.service;

import com.thief.entity.Account;
import com.thief.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions();

    Transaction transfer(String fromAccountId, String toAccountId, Double amount);

    Transaction transfer(Account fromAccount, Account toAccount, Double amount);
    
    Transaction revert(String transactionId);
}
