package com.thief.service.account;

import com.thief.service.transaction.ThiefException;

public class AccountAttributeNotValid extends ThiefException {

    private static final String ERROR_CODE = "ACCOUNT_ATTRIBUTE_NOT_VALID";

    public AccountAttributeNotValid(String attributeName) {
        super(String.format("value of %s, is null or empty, not valid", attributeName), ERROR_CODE);
    }
}
