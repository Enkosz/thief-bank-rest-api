package com.thief.controller.api.mapper;

import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import com.thief.controller.api.dto.transaction.TransactionDto;
import com.thief.entity.Transaction;

public class TransactionMapper {

    public static TransactionCompactDto fromDomainToTransactionCompactDto(Transaction transaction) {
        TransactionCompactDto transactionCompactDto = new TransactionCompactDto();

        transactionCompactDto.setId(transaction.getId());
        transactionCompactDto.setDate(transaction.getDate());

        return transactionCompactDto;
    }

    public static TransactionDto fromDomainToTransactionDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setId(transaction.getId());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setFromAccountId(transaction.getFrom().getId());
        transactionDto.setToAccountId(transaction.getTo().getId());
        transactionDto.setAmount(transaction.getAmount());

        return transactionDto;
    }
}