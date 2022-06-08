package com.thief.controller;

import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.service.AccountService;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
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
    public List<AccountDto> getAccounts() {
        return accountService.getAccounts()
                .stream()
                .map(AccountDto::fromDomainToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public AccountDto createAccount(@RequestBody CreateAccountDto createAccountDto) {
        Account account = accountService.createAccount(createAccountDto);

        return AccountDto.fromDomainToDto(account);
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
    }

    @GetMapping("/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        return accountService.getAccountById(accountId)
                .orElseThrow(RuntimeException::new);
    }


    @PostMapping(value = "/{accountId}", consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addDeposit(@PathVariable String accountId, @RequestBody Map<String, String> values) {
        Double amount = Double.parseDouble(values.get("amount"));

        accountService.deposit(accountId, amount);
    }

    @PutMapping("/{accountId}")
    public Account updateAccount(@PathVariable String accountId, @RequestAttribute String name, @RequestAttribute String surname) {
        return accountService.updateAccount(accountId, name, surname);
    }

    @PatchMapping("/{accountId}")
    public Account patchAccount(@PathVariable String accountId, @RequestAttribute String name, @RequestAttribute String surname) {
        return accountService.updateAccount(accountId, name, surname);
    }

    /*@RequestMapping(method = RequestMethod.HEAD, path = "/{accountId}")
    public Account getAccountInformation(@PathVariable String accountId) {
        return accountService.getAccountById(accountId);
    }*/
}
