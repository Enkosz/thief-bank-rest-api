package com.thief;

import com.thief.controller.api.dto.account.CreateAccountDto;
import com.thief.entity.Account;
import com.thief.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThiefBankApplication implements CommandLineRunner {

    static Logger logger = LoggerFactory.getLogger(ThiefBankApplication.class);

    @Autowired
    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(ThiefBankApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createAccount("Pipo", "Duro", 500d);
        createAccount("Zuppa", "Zupposa", 250d);
    }

    public void createAccount(String name, String surname, Double amount) {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setName(name);
        createAccountDto.setSurname(surname);

        Account account = accountService.createAccount(createAccountDto);
        logger.info(String.format("Created account %s %s with id: %s", account.getName(), account.getSurname(), account.getId()));

        accountService.deposit(account.getId(), amount);
    }
}
