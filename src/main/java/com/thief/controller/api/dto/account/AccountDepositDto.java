package com.thief.controller.api.dto.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class AccountDepositDto {

    @NotNull
    @DecimalMax("10.0")
    @DecimalMin("0.0")
    private Double amount;

    @NotNull
    @NotBlank
    private String transactionId;
}
