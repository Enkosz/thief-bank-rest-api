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
}