package com.thief.controller.api.dto.transaction;

import lombok.Data;

@Data
public class TransferDto {

    private String fromAccountId;
    private String toAccountId;
    private Double amount;
}
