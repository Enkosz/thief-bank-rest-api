package com.thief.service.account;

import com.thief.controller.api.dto.transaction.TransferDto;
import com.thief.controller.api.dto.account.AccountDepositDto;
import com.thief.controller.api.dto.account.CreateAccountDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.AccountRepository;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
import com.thief.service.transaction.InvalidTransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAllByActiveTrue();
    }

    @Override
    public Optional<Account> getAccountById(String accountId) {
        return accountRepository.findByIdAndActiveTrue(accountId);
    }

    @Override
    public Account createAccount(CreateAccountDto createAccountDto) {
        Account account = new Account();

        account.setName(createAccountDto.getName());
        account.setSurname(createAccountDto.getSurname());

        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(String accountId, String name, String surname) {
        Account account = accountRepository.findByIdAndActiveTrue(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if(name != null && !name.isEmpty()) account.setName(name);
        if(surname != null && !surname.isEmpty()) account.setSurname(surname);

        return accountRepository.save(account);
    }

    @Override
    public Account deleteAccount(String accountId) {
        Account account = accountRepository.findByIdAndActiveTrue(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.disable();
        return accountRepository.save(account);
    }

    @Override
    public AccountDepositDto deposit(String accountId, Double amount) {
        Account account = accountRepository.findByIdAndActiveTrue(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        Transaction transaction = transactionService.transfer(account, account, amount, Transaction.Type.INTERNAL);
        account.setAmount(account.getAmount() + amount);
        if(amount > 0)
            account.getTransactionsReceived().add(transaction);
        else
            account.getTransactionsSent().add(transaction);
        accountRepository.save(account);

        return new AccountDepositDto(account.getAmount(), transaction.getId());
    }

    @Override
    public Transaction transfer(TransferDto transfer) {
        if(transfer.getAmount() < 0)
            throw new InvalidTransactionException("Invalid transaction with not positive amount", "INVALID_TRANSACTION");

        Account fromAccount = accountRepository.findByIdAndActiveTrue(transfer.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(transfer.getFromAccountId()));
        Account toAccount = accountRepository.findByIdAndActiveTrue(transfer.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(transfer.getToAccountId()));
        Double amount = transfer.getAmount();

        Transaction transaction = transactionService.transfer(fromAccount, toAccount, amount, Transaction.Type.EXTERNAL);
        fromAccount.setAmount(fromAccount.getAmount() - amount);
        fromAccount.getTransactionsSent().add(transaction);
        toAccount.setAmount(toAccount.getAmount() + amount);
        toAccount.getTransactionsReceived().add(transaction);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return transaction;
    }

    @Override
    public Transaction revert(String transferId) {
        Transaction transaction = transactionService.getTransaction(transferId);

        TransferDto transferDto = new TransferDto();
        transferDto.setToAccountId(transaction.getFrom().getId());
        transferDto.setFromAccountId(transaction.getTo().getId());
        transferDto.setAmount(transaction.getAmount());

        return transfer(transferDto);
    }
}