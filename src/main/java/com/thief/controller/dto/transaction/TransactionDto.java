package com.thief.controller.dto.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionDto extends TransactionCompactDto {

    private String fromAccountId;
    private String toAccountId;
    private Double amount;
}
