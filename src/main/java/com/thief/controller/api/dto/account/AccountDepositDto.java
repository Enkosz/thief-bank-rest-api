package com.thief.controller.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountDepositDto {

    private Double amount;
    private String transactionId;
}
