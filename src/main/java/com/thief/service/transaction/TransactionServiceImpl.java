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
    public Transaction getTransaction(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new InvalidTransactionException(
                        String.format("Cannot find transaction with id %s", transactionId),
                        InvalidTransactionException.TRANSACTION_NOT_FOUND_CODE));
    }


    @Override
    public Transaction transfer(Account fromAccount, Account toAccount, Double amount, Transaction.Type type) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        amount = Math.abs(amount);

        if((type == Transaction.Type.EXTERNAL || (type == Transaction.Type.INTERNAL && transaction.getAmount() < 0)) && fromAccount.getAmount() - amount < 0) {
            throw new InvalidTransactionException(
                    String.format("Unable to create transaction, account %s has amount less then %s",
                                fromAccount.getId(), amount),
                                InvalidTransactionException.INVALID_TRANSACTION_CODE);
        }

        transaction.setFrom(fromAccount);
        transaction.setTo(toAccount);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction revert(String transactionId) {
        Transaction targetTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new InvalidTransactionException(
                        String.format("Cannot find transaction with id %s", transactionId),
                        InvalidTransactionException.TRANSACTION_NOT_FOUND_CODE));

        if(targetTransaction.getType() == Transaction.Type.INTERNAL)
            throw new InvalidTransactionException(
                    String.format("Cannot divert internal transaction %s", transactionId),
                    InvalidTransactionException.INVALID_TRANSACTION_CODE);

        try {
            return transfer(targetTransaction.getTo(), targetTransaction.getFrom(), targetTransaction.getAmount(), Transaction.Type.EXTERNAL);
        }
        catch (InvalidTransactionException e) {
            throw new InvalidTransactionException(
                    e.getMessage() + "\n" + String.format("Cannot revert transaction with id %s", transactionId),
                    InvalidTransactionException.INVALID_TRANSACTION_CODE);
        }

    }
}
