package com.thief.controller.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountCompactDto extends AccountOwnerDto {

    protected String id;
    protected Double amount;
}
