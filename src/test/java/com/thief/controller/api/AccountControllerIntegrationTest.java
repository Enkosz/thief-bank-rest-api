package com.thief.controller.api;

import com.thief.ThiefBankApplication;
import com.thief.controller.api.dto.account.*;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.validation.ConstraintViolationException;
import java.util.*;

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
                this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                        HttpMethod.POST,
                        request,
                        AccountCreatedDto.class);

        //then
        assertNotNull(response.getBody());
        AccountCreatedDto accountCreatedDto = response.getBody();
        assertEquals(accountCreatedDto.getId(), account.getId());
    }

    @Test
    public void itShouldReturnErrorWhenCreateAccountWithWrongAttribute() {
        //given
        CreateAccountDto createAccountDtoNameIsNull = new CreateAccountDto();
        createAccountDtoNameIsNull.setName(null);
        createAccountDtoNameIsNull.setSurname("ue");

        CreateAccountDto createAccountDtoNameIsEmpty = new CreateAccountDto();
        createAccountDtoNameIsEmpty.setName("");
        createAccountDtoNameIsEmpty.setSurname("ue");

        CreateAccountDto createAccountDtoSurnameIsNull = new CreateAccountDto();
        createAccountDtoSurnameIsNull.setName("ue");
        createAccountDtoSurnameIsNull.setSurname(null);

        CreateAccountDto createAccountDtoSurnameIsEmpty = new CreateAccountDto();
        createAccountDtoSurnameIsEmpty.setName("ue");
        createAccountDtoSurnameIsEmpty.setSurname("");

        //when
        ResponseEntity<String> responseNameEmpty =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                        HttpMethod.POST,
                        new HttpEntity<>(createAccountDtoNameIsEmpty),
                        String.class);
        ResponseEntity<String> responseNameNull =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                        HttpMethod.POST,
                        new HttpEntity<>(createAccountDtoNameIsNull),
                        String.class);
        ResponseEntity<String> responseSurnameEmpty =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                        HttpMethod.POST,
                        new HttpEntity<>(createAccountDtoSurnameIsEmpty),
                        String.class);
        ResponseEntity<String> responseSurnameNull =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account",
                        HttpMethod.POST,
                        new HttpEntity<>(createAccountDtoSurnameIsNull),
                        String.class);

        //then
        assertNotNull(responseNameEmpty);
        assertEquals(responseNameEmpty.getStatusCode(), HttpStatus.BAD_REQUEST);
        String code = responseNameEmpty.getBody().split("\"")[3];
        assertEquals(code, "VALIDATION_ERROR");

        assertNotNull(responseNameNull);
        assertEquals(responseNameNull.getStatusCode(), HttpStatus.BAD_REQUEST);
        code = responseNameNull.getBody().split("\"")[3];
        assertEquals(code, "VALIDATION_ERROR");

        assertNotNull(responseSurnameEmpty);
        assertEquals(responseSurnameEmpty.getStatusCode(), HttpStatus.BAD_REQUEST);
        code = responseSurnameEmpty.getBody().split("\"")[3];
        assertEquals(code, "VALIDATION_ERROR");

        assertNotNull(responseSurnameNull);
        assertEquals(responseSurnameNull.getStatusCode(), HttpStatus.BAD_REQUEST);
        code = responseSurnameNull.getBody().split("\"")[3];
        assertEquals(code, "VALIDATION_ERROR");
    }

    @Test
    public void itShouldReturnAccountWhenDeleteThatAccount() {
        //given
        Account account = new Account();
        account.setId("ue");
        account.setName("Fede");
        account.setSurname("Nero");
        account.setActive(false);

        Mockito.when(accountService.deleteAccount(ArgumentMatchers.any())).thenReturn(account);

        //when
        ResponseEntity<AccountCompactDto> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account?id=" + account.getId(),
                        HttpMethod.DELETE,
                        null,
                        AccountCompactDto.class);

        //then
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        AccountCompactDto accountCompactDto = response.getBody();
        assertNotNull(accountCompactDto);
        assertEquals(account.getId(), accountCompactDto.getId());
        assertEquals(account.getSurname(), accountCompactDto.getSurname());
        assertEquals(account.getName(), accountCompactDto.getName());
        assertEquals(account.getAmount(), accountCompactDto.getAmount());
    }

    @Test
    public void itShouldReturnErrorWhenDeleteButIdIsEmpty() {
        //given

        //when
        ResponseEntity<String> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account?id=",
                        HttpMethod.DELETE,
                        null,
                        String.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().split("\"")[3], "VALIDATION_ERROR");
    }

    @Test
    public void itShouldUpdateAccountWhenUpdate() {
        //given
        Account accountPre = new Account();
        accountPre.setId("id");
        accountPre.setSurname("ue");
        accountPre.setName("ueeee");

        String surnameNew = "New ue";
        String nameNew = "New Ueeee";

        Account accountPost = new Account();
        accountPost.setId("id");
        accountPost.setSurname(surnameNew);
        accountPost.setName(nameNew);

        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setName(nameNew);
        accountUpdateDto.setSurname(surnameNew);

        HttpEntity<AccountUpdateDto> request = new HttpEntity<>(accountUpdateDto);

        Mockito.when(accountService
                    .updateAccount(ArgumentMatchers.any(), ArgumentMatchers.eq(nameNew), ArgumentMatchers.eq(surnameNew)))
                .thenReturn(accountPost);

        //when
        ResponseEntity<AccountCompactDto> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account/" + accountPre.getId(),
                        HttpMethod.PUT,
                        request,
                        AccountCompactDto.class);

        //then
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        AccountCompactDto accountCompactDto = response.getBody();
        assertEquals(accountCompactDto.getSurname(), surnameNew);
        assertEquals(accountCompactDto.getName(), nameNew);
    }

    @Test
    public void itShouldReturnErrorWhenNameIsEmpty() {
        //given
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto();
        accountUpdateDto.setName("");
        accountUpdateDto.setSurname("surname");

        HttpEntity<AccountUpdateDto> request = new HttpEntity<>(accountUpdateDto);

        //when
        ResponseEntity<String> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account/1234",
                        HttpMethod.PUT,
                        request,
                        String.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().split("\"")[3], "VALIDATION_ERROR");
    }

    @Test
    public void itShouldReturnAccountInformationWhenHead() {
        //given
        Account account = new Account();
        account.setId("1234");
        account.setName("Enkos");
        account.setSurname("Nero");

        Mockito.when(accountService.getAccountById(ArgumentMatchers.eq("1234"))).thenReturn(Optional.of(account));

        //when
        ResponseEntity<String> response =
                this.restTemplate.exchange("http://localhost:" + port + "/api/account/" + account.getId(),
                        HttpMethod.HEAD,
                        null,
                        String.class);

        // then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getHeaders().get("X-Sistema-Bancario").get(0),
                account.getName() + ";" + account.getSurname());
    }

    @Test
    public void itShouldCreateDepositWhenDeposit() {
        //given
        Double amount = 10d;

        Map<String, String> map = new HashMap<>();
        map.put("amount", amount + "");
        HttpEntity<Map<String, String>> request = new HttpEntity<>(map);

        AccountDepositDto accountDepositDto = new AccountDepositDto(amount, "idDeposit");
        Mockito.when(accountService.deposit(ArgumentMatchers.eq("1234"), ArgumentMatchers.eq(amount)))
                .thenReturn(accountDepositDto);

        //when
        ResponseEntity<AccountDepositDto> response = this.restTemplate.exchange("http://localhost:" + port + "/api/account/1234",
                HttpMethod.POST,
                request,
                AccountDepositDto.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        AccountDepositDto accountDepositDtoPost = response.getBody();
        assertEquals(accountDepositDtoPost.getTransactionId(), "idDeposit");
        assertEquals(accountDepositDtoPost.getAmount(), amount);
    }

}