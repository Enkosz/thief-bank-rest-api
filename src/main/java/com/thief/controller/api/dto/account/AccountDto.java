package com.thief.controller.api.dto.account;

import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountDto extends AccountCompactDto {

    private final List<TransactionCompactDto> transactions = new ArrayList<>();
}
