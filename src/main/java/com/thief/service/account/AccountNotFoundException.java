package com.thief.service.account;

import com.thief.service.transaction.ThiefException;

public class AccountNotFoundException extends ThiefException {

    private static final String ERROR_CODE = "ACCOUNT_NOT_FOUND";

    public AccountNotFoundException(String accountId) {
        super(String.format("Cannot find account with id %s", accountId), ERROR_CODE);
    }
}
