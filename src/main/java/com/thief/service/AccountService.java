package com.thief.service;

import com.thief.controller.api.dto.account.AccountDepositDto;
import com.thief.controller.api.dto.account.CreateAccountDto;
import com.thief.controller.api.dto.transaction.TransactionExtendDto;
import com.thief.controller.api.dto.transaction.TransferDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    List<Account> getAccounts();

    Optional<Account> getAccountById(String accountId);

    Account createAccount(CreateAccountDto createAccountDto);

    Account updateAccount(String accountId, String name, String surname);

    Account deleteAccount(String accountId);

    AccountDepositDto deposit(String accountId, Double amount);

    Transaction transfer(TransferDto transfer);
}
