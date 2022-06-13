package com.thief.controller.api;

import com.thief.ThiefBankApplication;
import com.thief.controller.api.dto.account.AccountCompactDto;
import com.thief.controller.api.dto.account.AccountCreatedDto;
import com.thief.controller.api.dto.account.AccountDto;
import com.thief.controller.api.dto.account.CreateAccountDto;
import com.thief.entity.Account;
import com.thief.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class AccountControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @MockBean
    private AccountService accountService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void itShouldReturnEmptyAccountListJsonWhenThereAreNoAccount() throws Exception {
        // given
        List<Account> accounts = new ArrayList<>();
        Mockito.when(accountService.getAccounts()).thenReturn(accounts);

        // when
        ResponseEntity<List<AccountCompactDto>> response = this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountCompactDto>>() {});

        // then
        assertNotNull(response.getBody());
        assertEquals(response.getBody().size(), 0);
    }

    @Test
    public void itShouldReturnAccountListJsonWhenThereAreAccounts() throws Exception {
        // given
        List<Account> accounts = new ArrayList<>();
        Account account = new Account();
        account.setId("1234");
        account.setName("Enkos");
        account.setSurname("Nero");
        accounts.add(account);

        Mockito.when(accountService.getAccounts()).thenReturn(accounts);

        // when
        ResponseEntity<List<AccountCompactDto>> response = this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AccountCompactDto>>() {});

        // then
        assertNotNull(response.getBody());
        assertEquals(response.getBody().size(), 1);
        AccountCompactDto responseAccount = response.getBody().get(0);
        assertEquals(responseAccount.getId(), account.getId());
        assertEquals(responseAccount.getName(), account.getName());
        assertEquals(responseAccount.getSurname(), account.getSurname());
    }



    @Test
    public void itShouldReturnAccountWhenIdIsCorrect() {
        //given
        Account account = new Account();
        account.setId("1234");
        account.setName("Enkos");
        account.setSurname("Nero");

        Mockito.when(accountService.getAccountById(ArgumentMatchers.any())).thenReturn(Optional.of(account));

        //when
        ResponseEntity<AccountCompactDto> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account/" + account.getId(),
                        HttpMethod.GET,
                        null,
                        AccountCompactDto.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getHeaders().get("X-Sistema-Bancario").get(0),
                account.getName() + ";" + account.getSurname());
        AccountCompactDto responseAccount = response.getBody();
        assertEquals(responseAccount.getId(), account.getId());
        assertEquals(responseAccount.getName(), account.getName());
        assertEquals(responseAccount.getSurname(), account.getSurname());
    }

    @Test
    public void itShouldReturnErrorWhenIdIsWrong() {
        //given
        Account account = new Account();
        account.setId("1234");
        account.setName("Enkos");
        account.setSurname("Nero");

        Mockito.when(accountService.getAccountById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        //when
        ResponseEntity<String> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account/123",
                        HttpMethod.GET,
                        null,
                        String.class);

        // then
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().split("\"")[3], "ACCOUNT_NOT_FOUND");
    }

    @Test
    public void itShouldReturnIdAccountWhenCreateAccount() {
        //given
        String name = "Enkos";
        String surname = "Nero";
        String id = "1234";

        Account account = new Account();
        account.setId(id);
        account.setName(name);
        account.setSurname(surname);

        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setName(name);
        createAccountDto.setSurname(surname);

        HttpEntity<CreateAccountDto> request = new HttpEntity<>(createAccountDto);

        Mockito.when(accountService.createAccount(ArgumentMatchers.any())).thenReturn(account);


        //when
        ResponseEntity<AccountCreatedDto> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account/",
                        HttpMethod.POST,
                        request,
                        AccountCreatedDto.class);

        //then
        assertNotNull(response.getBody());
        AccountCreatedDto accountCreatedDto = response.getBody();
        assertEquals(accountCreatedDto.getId(), account.getId());
    }


}