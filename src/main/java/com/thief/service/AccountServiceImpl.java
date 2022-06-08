package com.thief.service;

import com.thief.controller.CreateAccountDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.AccountRepository;
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
        return accountRepository.findAll();
    }

    @Override
    public Optional<Account> getAccountById(String accountId) {
        return accountRepository.findById(accountId);
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
        return null;
    }

    @Override
    public Account deleteAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(RuntimeException::new);

        accountRepository.delete(account);
        return account;
    }

    @Override
    public void deposit(String accountId, Double amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(RuntimeException::new);

        account.setAmount(account.getAmount() + amount);

        Transaction transaction = transactionService.transfer(accountId, accountId, amount);
        account.getTransactions().add(transaction);

        accountRepository.save(account);
    }
}
