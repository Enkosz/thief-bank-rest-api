package com.thief.service.transaction;

import com.thief.controller.api.dto.transaction.TransactionExtendDto;
import com.thief.controller.api.mapper.TransactionMapper;
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
        transaction.setAmount(amount);
        amount = Math.abs(amount);

        if(fromAccount != toAccount || (fromAccount == toAccount && transaction.getAmount() < 0)){
            if (fromAccount.getAmount() - amount < 0)
                throw new InvalidTransactionException(
                        String.format("Unable to create transaction, account %s has amount less then %s",
                                fromAccount.getId(), amount),
                        InvalidTransactionException.INVALID_TRANSACTION_CODE);
            fromAccount.setAmount(fromAccount.getAmount() - amount);
            fromAccount.getTransactions().add(transaction);
        }

        if(fromAccount != toAccount || (fromAccount == toAccount && transaction.getAmount() > 0)) {
            toAccount.setAmount(toAccount.getAmount() + amount);
            //toAccount.getTransactions().add(transaction);
            // con questa si rompe tutto :(
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
        try{
            return transfer(targetTransaction.getTo(), targetTransaction.getFrom(), targetTransaction.getAmount());
        }
        catch (InvalidTransactionException e) {
            throw new InvalidTransactionException(
                    e.getMessage() + "\n" + String.format("Cannot revert transaction with id %s", transactionId),
                    InvalidTransactionException.INVALID_TRANSACTION_CODE);
        }

    }
}
