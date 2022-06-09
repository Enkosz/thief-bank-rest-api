package com.thief.controller.api.dto.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionExtendDto extends TransactionCompactDto {
    private String fromAccountId;
    private String toAccountId;
    private Double fromAccountAmount;
    private Double toAccountAmount;

}
