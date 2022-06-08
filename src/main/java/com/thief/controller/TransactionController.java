package com.thief.controller;

import com.thief.entity.Transaction;
import com.thief.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/transactions")
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestAttribute("from") String fromAccountId,
        @RequestAttribute("to") String toAccountId,
        @RequestAttribute("amount") Double amount) {
        return transactionService.transfer(fromAccountId, toAccountId, amount);
    }

    @PostMapping("/divert")
    public Transaction revert(@RequestAttribute("id") String idTransaction){
        return transactionService.revert(idTransaction);
    }
}
