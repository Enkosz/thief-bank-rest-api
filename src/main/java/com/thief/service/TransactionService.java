package com.thief.service;

import com.thief.entity.Account;
import com.thief.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions();

    Transaction transfer(Account fromAccount, Account toAccount, Double amount);

    Transaction revert(String transactionId);
}
