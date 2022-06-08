package com.thief.controller;

import com.thief.entity.Account;
import lombok.Data;

@Data
public class AccountDto {

    private String id;
    private String name;
    private String surname;
    private Double amount;

    public static AccountDto fromDomainToDto(Account account) {
        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setName(account.getName());
        accountDto.setSurname(account.getSurname());
        accountDto.setAmount(account.getAmount());

        return accountDto;
    }
}
