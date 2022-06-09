package com.thief.controller;

import com.thief.controller.dto.transaction.TransferDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody TransferDto transferDto) {
        Account fromAccount = accountService.getAccountById(transferDto.getFromAccountId())
                .orElseThrow(IllegalArgumentException::new);
        Account toAccount = accountService.getAccountById(transferDto.getToAccountId())
                .orElseThrow(IllegalArgumentException::new);

        return transactionService.transfer(fromAccount, toAccount, transferDto.getAmount());
    }

    @PostMapping("/divert")
    public Transaction revert(@RequestAttribute("id") String idTransaction){
        return transactionService.revert(idTransaction);
    }
}
