package com.thief.controller;

import com.thief.controller.dto.account.*;
import com.thief.controller.mapper.AccountMapper;
import com.thief.entity.Account;
import com.thief.service.AccountService;
import com.thief.service.account.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    public final AccountService accountService;

    @GetMapping
    public List<AccountCompactDto> getAccounts() {
        return accountService.getAccounts()
                .stream()
                .map(AccountMapper::fromDomainToAccountCompactDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(@PathVariable String accountId) {
        return accountService.getAccountById(accountId)
                .map(AccountMapper::fromDomainToAccountDto)
                .orElseThrow(AccountNotFoundException::new);
    }

    @PostMapping
    public AccountCreatedDto createAccount(@RequestBody CreateAccountDto createAccountDto) {
        Account account = accountService.createAccount(createAccountDto);

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        accountCreatedDto.setId(account.getId());

        return accountCreatedDto;
    }

    @DeleteMapping("/{accountId}")
    public AccountCompactDto deleteAccount(@PathVariable String accountId) {
        Account account = accountService.deleteAccount(accountId);

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @PostMapping(value = "/{accountId}", consumes = "application/json")
    public AccountDepositDto addDeposit(@PathVariable String accountId, @RequestBody Map<String, String> values) {
        Double amount = Double.parseDouble(values.get("amount"));

        return accountService.deposit(accountId, amount);
    }

    @PutMapping("/{accountId}")
    public AccountCompactDto updateAccount(@PathVariable String accountId, @RequestAttribute String name, @RequestAttribute String surname) {
        Account account = accountService.updateAccount(accountId, name, surname);

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @PatchMapping("/{accountId}")
    public AccountCompactDto patchAccount(@PathVariable String accountId, @RequestAttribute String name, @RequestAttribute String surname) {
        Account account = accountService.updateAccount(accountId, name, surname);

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/{accountId}")
    public AccountOwnerDto getAccountInformation(@PathVariable String accountId) {
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(AccountNotFoundException::new);

        return AccountMapper.fromDomainToAccountOwnerDto(account);
    }
}
