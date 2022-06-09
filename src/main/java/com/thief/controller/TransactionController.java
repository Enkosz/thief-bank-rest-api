package com.thief.controller;

import com.thief.controller.dto.transaction.TransactionCompactDto;
import com.thief.controller.dto.transaction.TransactionDto;
import com.thief.controller.dto.transaction.TransferDto;
import com.thief.controller.mapper.TransactionMapper;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @GetMapping("/transactions")
    public List<TransactionCompactDto> getTransactions() {
        return transactionService.getTransactions()
                .stream()
                .map(TransactionMapper::fromDomainToTransactionCompactDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/transfer")
    public TransactionDto transfer(@RequestBody TransferDto transferDto) {
        Account fromAccount = accountService.getAccountById(transferDto.getFromAccountId())
                .orElseThrow(IllegalArgumentException::new);
        Account toAccount = accountService.getAccountById(transferDto.getToAccountId())
                .orElseThrow(IllegalArgumentException::new);

        Transaction transaction = transactionService.transfer(fromAccount, toAccount, transferDto.getAmount());
        return TransactionMapper.fromDomainToTransactionDto(transaction);
    }

    @PostMapping("/divert")
    public TransactionDto revert(@RequestAttribute("id") String idTransaction){
        Transaction transaction = transactionService.revert(idTransaction);

        return TransactionMapper.fromDomainToTransactionDto(transaction);
    }
}
