package com.thief.controller.api.dto.account;

import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDto extends AccountCompactDto {

    private final Set<TransactionCompactDto> transactions = new HashSet<>();
}
