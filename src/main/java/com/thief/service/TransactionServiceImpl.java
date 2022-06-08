package com.thief.service;

import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction transfer(String fromAccountId, String toAccountId, Double amount) {
        return null;
    }

    @Override
    public Transaction transfer(Account fromAccount, Account toAccount, Double amount) {
        Transaction transaction = new Transaction();

        transaction.setFrom(fromAccount);
        transaction.setTo(toAccount);
        transaction.setAmount(amount);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction revert(String transactionId) {
        return null;
    }
}
