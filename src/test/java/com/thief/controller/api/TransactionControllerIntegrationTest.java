package com.thief.controller.api;

import com.thief.controller.api.dto.account.AccountDepositDto;
import com.thief.controller.api.dto.transaction.TransactionCompactDto;
import com.thief.controller.api.dto.transaction.TransactionExtendDto;
import com.thief.controller.api.dto.transaction.TransferDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class TransactionControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void itShouldReturnEmptyListWhenThereAreNoTransaction() {
        //given
        Mockito.when(transactionService.getTransactions()).thenReturn(new ArrayList<>());

        //when
        ResponseEntity<List<Transaction>> response = this.restTemplate.exchange("http://localhost:" + port + "/api/transactions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Transaction>>() {});

        //given
        assertNotNull(response);
        assertEquals(response.getBody().size(), 0);
    }

    @Test
    public void itShouldReturnTransactionsListWhenThereAreTransaction() {
        //given
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setId("idTransaction");
        transaction.setAmount(10d);
        Account toAccount = new Account();
        toAccount.setId("to");
        Account fromAccount = new Account();
        fromAccount.setId("from");
        transaction.setTo(toAccount);
        transaction.setFrom(fromAccount);
        transactions.add(transaction);

        Mockito.when(transactionService.getTransactions()).thenReturn(transactions);

        //when
        ResponseEntity<List<TransactionCompactDto>> response = this.restTemplate.exchange("http://localhost:" + port + "/api/transactions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionCompactDto>>() {});

        //given
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().size(), 1);
        TransactionCompactDto transactionReturn = response.getBody().get(0);
        assertEquals(transactionReturn.getId(), transaction.getId());
    }


    @Test
    public void itShouldCreateTransactionWhenTransfer() {
        //given
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(10d);
        transferDto.setFromAccountId("from");
        transferDto.setToAccountId("to");

        Account accountFrom = new Account();
        accountFrom.setId("from");
        accountFrom.setAmount(0d);
        Account accountTo = new Account();
        accountTo.setId("to");
        accountTo.setAmount(10d);

        Transaction transaction = new Transaction();
        transaction.setId("idTrans");
        transaction.setTo(accountTo);
        transaction.setFrom(accountFrom);
        transaction.setAmount(10d);

        Mockito.when(accountService.transfer(ArgumentMatchers.eq(transferDto))).thenReturn(transaction);

        HttpEntity<TransferDto> request = new HttpEntity<>(transferDto);

        //when
        ResponseEntity<TransactionExtendDto> response = this.restTemplate.exchange("http://localhost:" + port + "/api/transfer",
                HttpMethod.POST,
                request,
                TransactionExtendDto.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        TransactionExtendDto transactionExtendDto = response.getBody();
        assertEquals(transactionExtendDto.getId(), transaction.getId());
        assertEquals(transactionExtendDto.getFromAccountAmount(), transaction.getFrom().getAmount());
        assertEquals(transactionExtendDto.getFromAccountId(), transaction.getFrom().getId());
        assertEquals(transactionExtendDto.getToAccountAmount(), transaction.getTo().getAmount());
        assertEquals(transactionExtendDto.getToAccountId(), transaction.getTo().getId());
    }

    @Test
    public void itShouldCreateTransactionRevertWhenDivert() {
        //given
        Account accountFrom = new Account();
        accountFrom.setId("from");
        accountFrom.setAmount(0d);
        Account accountTo = new Account();
        accountTo.setId("to");
        accountTo.setAmount(10d);

        Transaction transaction = new Transaction();
        transaction.setId("idTrans");
        transaction.setTo(accountTo);
        transaction.setFrom(accountFrom);
        transaction.setAmount(10d);

        Transaction transactionRevert = new Transaction();
        transactionRevert.setId("idTransRevert");
        transactionRevert.setAmount(transaction.getAmount());
        transactionRevert.setFrom(transaction.getTo());
        transactionRevert.setTo(transaction.getFrom());

        Map<String, String> body = new HashMap<>();
        body.put("id", transaction.getId());

        Mockito.when(accountService.revert(ArgumentMatchers.eq(transaction.getId()))).thenReturn(transactionRevert);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body);

        //when
        ResponseEntity<TransactionExtendDto> response = this.restTemplate.exchange("http://localhost:" + port + "/api/divert",
                HttpMethod.POST,
                request,
                TransactionExtendDto.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        TransactionExtendDto transactionExtendDto = response.getBody();
        assertEquals(transactionExtendDto.getId(), transactionRevert.getId());
        assertEquals(transactionExtendDto.getFromAccountAmount(), transaction.getTo().getAmount());
        assertEquals(transactionExtendDto.getFromAccountId(), transaction.getTo().getId());
        assertEquals(transactionExtendDto.getToAccountAmount(), transaction.getFrom().getAmount());
        assertEquals(transactionExtendDto.getToAccountId(), transaction.getFrom().getId());
    }

}