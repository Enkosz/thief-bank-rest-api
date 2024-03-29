package com.thief.service;

import com.thief.entity.Account;
import com.thief.entity.Transaction;

import java.util.List;

public interface TransactionService {

    List<Transaction> getTransactions();

    Transaction getTransaction(String transactionId);

    Transaction transfer(Account fromAccount, Account toAccount, Double amount, Transaction.Type type);

    Transaction revert(String transactionId);
}
