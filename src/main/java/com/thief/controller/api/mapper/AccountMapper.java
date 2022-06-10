package com.thief.controller.api.mapper;

import com.thief.controller.api.dto.account.AccountCompactDto;
import com.thief.controller.api.dto.account.AccountDto;
import com.thief.controller.api.dto.account.AccountOwnerDto;
import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import com.thief.controller.api.dto.transaction.TransactionDto;
import com.thief.controller.api.dto.transaction.TransactionExtendDto;
import com.thief.entity.Account;

import java.util.Comparator;
import java.util.List;
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

        List<TransactionDto> transactionDtoList = account.getTransactions()
                .stream()
                .map(TransactionMapper::fromDomainToTransactionDto)
                .sorted(Comparator.comparing(TransactionCompactDto::getDate))
                .collect(Collectors.toList());

        accountDto.getTransactions()
                .addAll(transactionDtoList);
        return accountDto;
    }

    public static AccountOwnerDto fromDomainToAccountOwnerDto(Account account) {
        AccountOwnerDto accountOwnerDto = new AccountOwnerDto();

        accountOwnerDto.setName(account.getName());
        accountOwnerDto.setSurname(account.getSurname());

        return accountOwnerDto;
    }
}
