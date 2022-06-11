package com.thief.controller.api;

import com.thief.ThiefBankApplication;
import com.thief.controller.api.dto.account.AccountCompactDto;
import com.thief.entity.Account;
import com.thief.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

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
    public void itShouldAccountListJsonWhenThereAreAccounts() throws Exception {
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
}