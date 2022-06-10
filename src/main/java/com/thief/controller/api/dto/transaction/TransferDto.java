package com.thief.controller.api.dto.transaction;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class TransferDto {

    @NotNull
    @NotBlank
    private String fromAccountId;

    @NotNull
    @NotBlank
    private String toAccountId;

    @DecimalMax("10.0")
    @DecimalMin("0.0")
    @NotNull
    @Positive
    private Double amount;
}
