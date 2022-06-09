package com.thief.controller.api.dto.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountCompactDto extends AccountOwnerDto {

    protected String id;
    protected Double amount;
}
