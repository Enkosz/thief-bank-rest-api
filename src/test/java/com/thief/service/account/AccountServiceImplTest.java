package com.thief.service.account;

import com.thief.controller.api.dto.account.AccountDepositDto;
import com.thief.controller.api.dto.account.CreateAccountDto;
import com.thief.controller.api.dto.transaction.TransferDto;
import com.thief.entity.Account;
import com.thief.entity.Transaction;
import com.thief.repository.AccountRepository;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
import com.thief.service.transaction.InvalidTransactionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @Captor
    ArgumentCaptor<Account> accountArgumentCaptor;

    @Test
    public void itShouldCreateAnAccountWhenTheCreateAccountDtoIsValid() {
        // given
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setName("Zuppa");
        createAccountDto.setSurname("Di Culo");

        // when
        accountService.createAccount(createAccountDto);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        // then
        Account createdAccount = accountArgumentCaptor.getValue();

        assertNotNull(createdAccount);
        assertEquals(createdAccount.getName(), createAccountDto.getName());
        assertEquals(createdAccount.getSurname(), createAccountDto.getSurname());
        assertEquals(createdAccount.getAmount(), 0);
    }

    @Test
    public void itShouldSoftDeleteAnAccountWhenIsDeleted() {
        // given
        Account account = new Account();
        account.setId("123");
        account.setName("Zuppa");
        account.setSurname("Di culo");

        Mockito.when(accountRepository.findByIdAndActiveTrue(Mockito.anyString())).thenReturn(Optional.of(account));

        // when
        accountService.deleteAccount(account.getId());
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        // then
        Account deletedAccount = accountArgumentCaptor.getValue();

        assertFalse(deletedAccount.getActive());
    }

    @Test
    public void itShouldUpdateTheAccountWhenIdIsValid () {

        Account account = new Account();
        account.setName("Zuppa");
        account.setSurname("Di Culo");
        String name = "newZuppa";
        String surname = "newDi Culo";
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));

        //when
        accountService.updateAccount(account.getId(), name, surname);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());
        //then
        Account updateAccount = accountArgumentCaptor.getValue();

        assertNotNull(updateAccount);
        assertEquals(updateAccount.getName(), name);
        assertEquals(updateAccount.getSurname(), surname);
    }

    @Test
    public void itShouldChangeOnlyNameWhenSurnameIsNull () {
        //given
        String name = "Zuppa";
        String surname = "Di Culo";

        Account account = new Account();
        account.setName(name);
        account.setSurname(surname);
        String newName = "newZuppa";
        String surnameEmpty = "";
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));

        //when
        accountService.updateAccount(account.getId(), newName, surnameEmpty);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account updateAccount = accountArgumentCaptor.getValue();
        assertNotNull(updateAccount);
        assertEquals(updateAccount.getName(), newName);
        assertEquals(updateAccount.getSurname(), surname);
    }

    @Test
    public void itShouldChangeOnlyNameWhenSurnameIsEmpty () {
        //given
        String name = "Zuppa";
        String surname = "Di Culo";

        Account account = new Account();
        account.setName(name);
        account.setSurname(surname);
        String newName = "newZuppa";
        String surnameEmpty = null;
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));

        //when
        accountService.updateAccount(account.getId(), newName, surnameEmpty);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account updateAccount = accountArgumentCaptor.getValue();
        assertNotNull(updateAccount);
        assertEquals(updateAccount.getName(), newName);
        assertEquals(updateAccount.getSurname(), surname);
    }

    @Test
    public void itShouldChangeOnlySurnameWhenNameIsNull () {
        //given
        String name = "Zuppa";
        String surname = "Di Culo";

        Account account = new Account();
        account.setName(name);
        account.setSurname(surname);
        String nameEmpty = "";
        String newSurname = "New Di culo";
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));

        //when
        accountService.updateAccount(account.getId(), nameEmpty, newSurname);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account updateAccount = accountArgumentCaptor.getValue();
        assertNotNull(updateAccount);
        assertEquals(updateAccount.getName(), name);
        assertEquals(updateAccount.getSurname(), newSurname);
    }

    @Test
    public void itShouldChangeOnlySurnameWhenNameIsEmpty () {
        //given
        String name = "Zuppa";
        String surname = "Di Culo";

        Account account = new Account();
        account.setName(name);
        account.setSurname(surname);
        String nameEmpty = null;
        String newSurname = "New Di culo";
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));

        //when
        accountService.updateAccount(account.getId(), nameEmpty, newSurname);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account updateAccount = accountArgumentCaptor.getValue();
        assertNotNull(updateAccount);
        assertEquals(updateAccount.getName(), name);
        assertEquals(updateAccount.getSurname(), newSurname);
    }

    @Test
    public void itShouldIncreaseAmountOfAccountWhenHeDeposit() {
        //given
        Account account = new Account();
        account.setName("Zuppa");
        account.setSurname("Di Culo");
        account.setAmount(0d);
        Double increasedAmount = 100d;
        Transaction transaction = new Transaction();
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));
        Mockito.when(transactionService.transfer(ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any()))
                .thenReturn(transaction);

        //when
        accountService.deposit(new String(), increasedAmount);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account accountUpdated = accountArgumentCaptor.getValue();
        assertNotNull(accountUpdated);
        assertEquals(accountUpdated.getAmount(), increasedAmount);
        assertEquals(accountUpdated.getTransactionsReceived().size(), 1);
    }

    @Test
    public void itShouldDecreaseAmountOfAccountWhenHeDeposit() {
        //given
        Account account = new Account();
        account.setName("Zuppa");
        account.setSurname("Di Culo");
        Double amount = 90d;
        account.setAmount(amount);
        Double decreasedAmount = -10d;
        Transaction transaction = new Transaction();
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(account));
        Mockito.when(transactionService.transfer(ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any()))
                .thenReturn(transaction);

        //when
        accountService.deposit("", decreasedAmount);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account accountUpdated = accountArgumentCaptor.getValue();
        assertNotNull(accountUpdated);
        assertEquals(accountUpdated.getAmount(), amount + decreasedAmount);
        assertEquals(accountUpdated.getTransactionsSent().size(), 1);
    }

    @Test
    public void itShouldThrowInvalidTransactionExceptionWhenHeDepositZero() {
        //given
        Account account = new Account();
        account.setName("Zuppa");
        account.setSurname("Di Culo");

        //when
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class, () -> accountService.deposit("", 0d));

        //then
        assertEquals(exception.getCode(), InvalidTransactionException.INVALID_TRANSACTION_CODE);
    }

    @Test
    public void itShouldAddTransferWhenDoTransfer() {
        //given
        Account accountTo = new Account();
        accountTo.setId("To");
        accountTo.setName("Zuppa");
        accountTo.setSurname("Di Culo");
        accountTo.setAmount(0d);
        Double amountTo = 0d;

        Account accountFrom = new Account();
        accountFrom.setId("From");
        accountFrom.setName("Zuppa");
        accountFrom.setSurname("Di Culo");
        accountFrom.setAmount(100d);
        Double amountFrom = 100d;

        Double amountUpdated = 10d;

        TransferDto transferDto = new TransferDto();
        transferDto.setToAccountId(accountTo.getId());
        transferDto.setFromAccountId(accountFrom.getId());
        transferDto.setAmount(amountUpdated);

        Transaction transaction = new Transaction();
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.same(accountTo.getId())))
                    .thenReturn(Optional.ofNullable(accountTo));
        Mockito.when(accountRepository.findByIdAndActiveTrue(ArgumentMatchers.same(accountFrom.getId())))
                    .thenReturn(Optional.ofNullable(accountFrom));
        Mockito.when(transactionService.transfer(ArgumentMatchers.any(),ArgumentMatchers.any(),ArgumentMatchers.any()))
                .thenReturn(transaction);

        //when
        accountService.transfer(transferDto);
        Mockito.verify(accountRepository, times(2)).save(accountArgumentCaptor.capture());

        //then
        Account accountFromUpdate = accountArgumentCaptor.getAllValues().get(0);
        Account accountToUpdate = accountArgumentCaptor.getAllValues().get(1);
        assertNotNull(accountToUpdate);
        assertEquals(accountToUpdate.getAmount(), amountTo + amountUpdated);
        assertEquals(accountToUpdate.getTransactionsReceived().size(), 1);
        assertNotNull(accountFromUpdate);
        assertEquals(accountFromUpdate.getAmount(), amountFrom - amountUpdated);
        assertEquals(accountFromUpdate.getTransactionsSent().size(), 1);
    }

    @Test
    public void itShouldThrowInvalidTransactionExceptionWhenDoTransferZero() {
        //given
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(0d);

        //when
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
                () -> accountService.transfer(transferDto));

        //then
        assertEquals(exception.getCode(), InvalidTransactionException.INVALID_TRANSACTION_CODE);
    }

    @Test
    public void itShouldThrowInvalidTransactionExceptionWhenDoTransferNegativeNumber() {
        //given
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(-1d);

        //when
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
                () -> accountService.transfer(transferDto));

        //then
        assertEquals(exception.getCode(), InvalidTransactionException.INVALID_TRANSACTION_CODE);
    }

    @Test
    public void itShouldThrowInvalidTransactionExceptionWhenAccountsAreTheSame() {
        //given
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(1d);
        transferDto.setFromAccountId("SameId");
        transferDto.setToAccountId("SameId");

        //when
        InvalidTransactionException exception = assertThrows(InvalidTransactionException.class,
                () -> accountService.transfer(transferDto));

        //then
        assertEquals(exception.getCode(), InvalidTransactionException.INVALID_TRANSACTION_CODE);
    }



}