package com.thief.controller.api.mapper;

import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import com.thief.controller.api.dto.transaction.TransactionDto;
import com.thief.controller.api.dto.transaction.TransactionExtendDto;
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
        transactionDto.setType(transaction.getType().name());

        return transactionDto;
    }

    public static TransactionExtendDto fromDomainToTransactionExtendDto(Transaction transaction) {
        TransactionExtendDto transactionExtendDto= new TransactionExtendDto();

        transactionExtendDto.setId(transaction.getId());
        transactionExtendDto.setDate(transaction.getDate());
        transactionExtendDto.setFromAccountId(transaction.getFrom().getId());
        transactionExtendDto.setToAccountId(transaction.getTo().getId());
        transactionExtendDto.setFromAccountAmount(transaction.getFrom().getAmount());
        transactionExtendDto.setToAccountAmount(transaction.getTo().getAmount());

        return transactionExtendDto;
    }
}
