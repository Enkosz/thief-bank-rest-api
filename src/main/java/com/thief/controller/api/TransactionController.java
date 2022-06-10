package com.thief.controller.api;

import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import com.thief.controller.api.dto.transaction.TransactionExtendDto;
import com.thief.controller.api.dto.transaction.TransferDto;
import com.thief.controller.api.mapper.TransactionMapper;
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
    public TransactionExtendDto transfer(@RequestBody TransferDto transferDto) {
        Transaction transaction = accountService.transfer(transferDto);

        return TransactionMapper.fromDomainToTransactionExtendDto(transaction);
    }

    @PostMapping("/divert")
    public TransactionExtendDto revert(@RequestAttribute("id") String idTransaction){
        Transaction transaction = transactionService.revert(idTransaction);

        return TransactionMapper.fromDomainToTransactionExtendDto(transaction);
    }
}
