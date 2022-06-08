package com.thief.controller.mapper;

import com.thief.controller.dto.transaction.TransactionCompactDto;
import com.thief.entity.Transaction;

public class TransactionMapper {

    public static TransactionCompactDto fromDomainToAccountCompactDto(Transaction transaction) {
        TransactionCompactDto transactionCompactDto = new TransactionCompactDto();

        transactionCompactDto.setId(transaction.getId());
        transactionCompactDto.setDate(transaction.getDate());

        return transactionCompactDto;
    }

}
