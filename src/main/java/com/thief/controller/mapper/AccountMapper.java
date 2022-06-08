package com.thief.controller.mapper;

import com.thief.controller.dto.account.AccountCompactDto;
import com.thief.controller.dto.account.AccountDto;
import com.thief.controller.dto.account.AccountOwnerDto;
import com.thief.entity.Account;

import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountCompactDto fromDomainToAccountCompactDto(Account account) {
        AccountCompactDto accountDto = new AccountCompactDto();

        accountDto.setId(account.getId());
        accountDto.setName(account.getName());
        accountDto.setSurname(account.getSurname());
        accountDto.setAmount(account.getAmount());

        return accountDto;
    }

    public static AccountDto fromDomainToAccountDto(Account account) {
        AccountCompactDto accountCompactDto = AccountMapper.fromDomainToAccountCompactDto(account);
        AccountDto accountDto = new AccountDto();

        accountDto.setId(accountCompactDto.getId());
        accountDto.setName(accountCompactDto.getName());
        accountDto.setSurname(accountCompactDto.getSurname());
        accountDto.setAmount(accountCompactDto.getAmount());

        accountDto.getTransactions()
                .addAll(account.getTransactions()
                        .stream()
                        .map(TransactionMapper::fromDomainToAccountCompactDto)
                        .collect(Collectors.toList()));

        return accountDto;
    }

    public static AccountOwnerDto fromDomainToAccountOwnerDto(Account account) {
        AccountOwnerDto accountOwnerDto = new AccountOwnerDto();

        accountOwnerDto.setName(account.getName());
        accountOwnerDto.setSurname(account.getSurname());

        return accountOwnerDto;
    }
}
