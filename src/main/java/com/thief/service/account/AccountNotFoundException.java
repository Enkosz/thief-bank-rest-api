package com.thief.service.account;

import com.thief.service.transaction.ThiefException;

public class AccountNotFoundException extends ThiefException {

    private static final String ERROR_CODE = "ACCOUNT_NOT_FOUND";

    public AccountNotFoundException() {
        super(ERROR_CODE);
    }
}
