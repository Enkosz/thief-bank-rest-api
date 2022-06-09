package com.thief.service.account;

import com.thief.controller.dto.account.AccountDepositDto;
import com.thief.controller.dto.account.CreateAccountDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.AccountRepository;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
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
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setName(name);
        account.setSurname(surname);

        return accountRepository.save(account);
    }

    @Override
    public Account deleteAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        accountRepository.delete(account);
        return account;
    }

    @Override
    public AccountDepositDto deposit(String accountId, Double amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        Transaction transaction = transactionService.transfer(account, account, amount);

        account.setAmount(account.getAmount() + amount);
        account.getTransactions().add(transaction);
        accountRepository.save(account);

        return new AccountDepositDto(account.getAmount(), transaction.getId());
    }
}
