package com.thief.service;

import com.thief.controller.CreateAccountDto;
import com.thief.entity.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> getAccounts();

    Optional<Account> getAccountById(String accountId);

    Account createAccount(CreateAccountDto createAccountDto);

    Account updateAccount(String accountId, String name, String surname);

    Account deleteAccount(String accountId);

    void deposit(String accountId, Double amount);
}
