package com.thief.service.transaction;

import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.TransactionRepository;
import com.thief.service.TransactionService;
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
    public Transaction transfer(Account fromAccount, Account toAccount, Double amount) {
        Transaction transaction = new Transaction();

        if (fromAccount.getAmount() - amount < 0)
            throw new InvalidTransactionException(
                    String.format("Unable to create transaction, account %s has amount less then %s",
                            fromAccount.getId(), Math.abs(amount)), InvalidTransactionException.INVALID_TRANSACTION_CODE);

        fromAccount.setAmount(fromAccount.getAmount() - amount);

        transaction.setFrom(fromAccount);
        transaction.setTo(toAccount);
        transaction.setAmount(amount);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction revert(String transactionId) {
        Transaction targetTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new InvalidTransactionException(
                        String.format("Cannot find transaction with id %s", transactionId),
                        InvalidTransactionException.TRANSACTION_NOT_FOUND_CODE));

        return transfer(targetTransaction.getTo(), targetTransaction.getFrom(), targetTransaction.getAmount());
    }
}
