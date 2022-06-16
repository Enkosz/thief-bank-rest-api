package com.thief.service.transaction;

import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.TransactionRepository;
import com.thief.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Captor
    ArgumentCaptor<Transaction> transactionArgumentCaptor;

    @Test
    public void itShouldCreateNewTransferWhenDeposit() {
        //given
        Account account = new Account();
        account.setId("ue");
        account.setName("Enk");
        account.setSurname("OS");
        account.setAmount(0d);
        Double amountDeposit = 10d;

        //when
        transactionService.transfer(account, account, amountDeposit, Transaction.Type.INTERNAL);
        Mockito.verify(transactionRepository).save(transactionArgumentCaptor.capture());

        //then
        Transaction deposit = transactionArgumentCaptor.getValue();
        assertNotNull(deposit);
        assertEquals(account.getId(), deposit.getTo().getId());
        assertEquals(account.getId(), deposit.getFrom().getId());
        assertEquals(deposit.getAmount(), amountDeposit);
    }

    @Test
    public void itShouldCreateNewTransferWhenWithdrawWithEnoughAmount() {
        //given
        Account account = new Account();
        account.setId("ue");
        account.setName("Enk");
        account.setSurname("OS");
        account.setAmount(10d);
        Double amountDeposit = -10d;

        //when
        transactionService.transfer(account, account, amountDeposit, Transaction.Type.EXTERNAL);
        Mockito.verify(transactionRepository).save(transactionArgumentCaptor.capture());

        //then
        Transaction deposit = transactionArgumentCaptor.getValue();
        assertNotNull(deposit);
        assertEquals(account.getId(), deposit.getTo().getId());
        assertEquals(account.getId(), deposit.getFrom().getId());
        assertEquals(deposit.getAmount(), amountDeposit);
    }

    @Test
    public void itShouldInvalidTransactionExceptionWhenWithdrawWithoutEnoughAmount() {
        //given
        Account account = new Account();
        account.setId("ue");
        account.setName("Enk");
        account.setSurname("OS");
        account.setAmount(0d);
        Double amountDeposit = -10d;

        //when
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transfer(account, account, amountDeposit, Transaction.Type.INTERNAL));

        //then
        assertEquals(InvalidTransactionException.INVALID_TRANSACTION_CODE, exception.getCode());
    }

    @Test
    public void itShouldCreateNewTransferWhenTransferWithEnoughAmount() {
        //given
        Account accountFrom = new Account();
        accountFrom.setId("ue");
        accountFrom.setName("Enk");
        accountFrom.setSurname("OS");
        accountFrom.setAmount(10d);

        Account accountTo = new Account();
        accountTo.setId("Cip");
        accountTo.setName("Fede");
        accountTo.setSurname("Ueee");
        accountTo.setAmount(0d);

        Double amountTransfer = 10d;

        //when
        transactionService.transfer(accountFrom, accountTo, amountTransfer, Transaction.Type.EXTERNAL);
        Mockito.verify(transactionRepository).save(transactionArgumentCaptor.capture());

        //then
        Transaction transfer = transactionArgumentCaptor.getValue();
        assertNotNull(transfer);
        assertEquals(accountTo.getId(), transfer.getTo().getId());
        assertEquals(accountFrom.getId(), transfer.getFrom().getId());
        assertEquals(amountTransfer, transfer.getAmount());
    }

    @Test
    public void itShouldThrowInvalidTransactionExceptionWhenTransferWithoutEnoughAmount() {
        //given
        Account accountFrom = new Account();
        accountFrom.setId("ue");
        accountFrom.setName("Enk");
        accountFrom.setSurname("OS");
        accountFrom.setAmount(0d);

        Account accountTo = new Account();
        accountTo.setId("Cip");
        accountTo.setName("Fede");
        accountTo.setSurname("Ueee");
        accountTo.setAmount(0d);

        Double amountTransfer = 10d;

        //when
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
                () -> transactionService.transfer(accountFrom, accountTo, amountTransfer, Transaction.Type.EXTERNAL));

        //then
        assertEquals(InvalidTransactionException.INVALID_TRANSACTION_CODE, exception.getCode());
    }


}