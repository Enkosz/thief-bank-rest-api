package com.thief.controller.api;

import com.sun.istack.NotNull;
import com.thief.controller.api.dto.account.*;
import com.thief.controller.api.mapper.AccountMapper;
import com.thief.entity.Account;
import com.thief.service.AccountService;
import com.thief.service.account.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Validated
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
    public ResponseEntity<AccountDto> getAccount(@PathVariable String accountId) {
        AccountDto account = accountService.getAccountById(accountId)
                            .map(AccountMapper::fromDomainToAccountDto)
                            .orElseThrow(() -> new AccountNotFoundException(accountId));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sistema-Bancario", account.getName() + ";" + account.getSurname());
        return new ResponseEntity<>(account, headers, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE )
    public AccountCreatedDto createAccount(@RequestBody @Valid CreateAccountDto createAccountDto) {
        Account account = accountService.createAccount(createAccountDto);

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        accountCreatedDto.setId(account.getId());

        return accountCreatedDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    public AccountCreatedDto createAccountFormEncoded(@Valid CreateAccountDto createAccountDto) {
        Account account = accountService.createAccount(createAccountDto);

        AccountCreatedDto accountCreatedDto = new AccountCreatedDto();
        accountCreatedDto.setId(account.getId());

        return accountCreatedDto;
    }

    @DeleteMapping
    public AccountCompactDto deleteAccount(@RequestParam("id") @NotNull @NotBlank String accountId) {
        Account account = accountService.deleteAccount(accountId);

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @PostMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public AccountDepositDto addDeposit(@PathVariable String accountId, @RequestBody Map<String, String> values) {
        Double amount = Double.parseDouble(values.get("amount"));

        return accountService.deposit(accountId, amount);
    }

    @PostMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    public AccountDepositDto addDepositFormEncoded(@PathVariable String accountId, Map<String, String> values) {
        Double amount = Double.parseDouble(values.get("amount"));

        return accountService.deposit(accountId, amount);
    }

    @PutMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountCompactDto updateAccount(@PathVariable String accountId, @RequestBody @Valid AccountUpdateDto accountUpdateDto) {
        Account account = accountService.updateAccount(accountId, accountUpdateDto.getName(), accountUpdateDto.getSurname());

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @PutMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public AccountCompactDto updateAccountFormEncoded(@PathVariable String accountId, @Valid AccountUpdateDto accountUpdateDto) {
        Account account = accountService.updateAccount(accountId, accountUpdateDto.getName(), accountUpdateDto.getSurname());

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @PatchMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE )
    public AccountCompactDto patchAccount(@PathVariable String accountId, @RequestBody @Valid AccountPatchDto accountPatchDto) {
        Account account = accountService.updateAccount(accountId, accountPatchDto.getName(), accountPatchDto.getSurname());

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @PatchMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    public AccountCompactDto patchAccountFormEncoded(@PathVariable String accountId, @Valid AccountPatchDto accountPatchDto) {
        Account account = accountService.updateAccount(accountId, accountPatchDto.getName(), accountPatchDto.getSurname());

        return AccountMapper.fromDomainToAccountCompactDto(account);
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/{accountId}")
    public ResponseEntity<String> getAccountInformation(@PathVariable String accountId) {
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Sistema-Bancario", account.getName() + ";" + account.getSurname());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
