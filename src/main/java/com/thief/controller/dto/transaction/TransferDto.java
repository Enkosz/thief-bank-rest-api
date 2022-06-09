package com.thief.controller.dto.transaction;

import lombok.Data;

@Data
public class TransferDto {

    private String fromAccountId;
    private String toAccountId;
    private Double amount;
}
