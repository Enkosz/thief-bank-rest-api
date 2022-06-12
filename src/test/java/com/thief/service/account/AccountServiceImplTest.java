package com.thief.service.account;

import com.thief.controller.api.dto.account.CreateAccountDto;
import com.thief.entity.Account;
import com.thief.repository.AccountRepository;
import com.thief.service.AccountService;
import com.thief.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    public void itShouldUpdateTheAccountWhenIdIsValid () {

        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setName("Zuppa");
        createAccountDto.setSurname("Di Culo");
        accountService.createAccount(createAccountDto);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());
        Account oldAccount = accountArgumentCaptor.getValue();
        String name = "newZuppa";
        String surname = "newDi Culo";
        Mockito.when(accountRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.ofNullable(oldAccount));

        //when
        accountService.updateAccount(oldAccount.getId(), name, surname);
        Mockito.verify(accountRepository).save(accountArgumentCaptor.capture());

        //then
        Account updateAccount = accountArgumentCaptor.getValue();

        assertNotNull(updateAccount);
        assertEquals(updateAccount.getName(), oldAccount.getName());
        assertEquals(updateAccount.getSurname(), oldAccount.getSurname());
    }


}