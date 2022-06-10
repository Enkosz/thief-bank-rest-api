package com.thief.service.account;

import com.thief.service.transaction.ThiefException;

public class AccountAttributeNotValid extends ThiefException {

    private static final String ERROR_CODE_NOT_VALID = "ACCOUNT_ATTRIBUTE_NOT_VALID";
    private static final String ERROR_CODE_ATTRIBUTE = "ACCOUNT_WRONG_NUMBER_OF_ATTRIBUTES";

    public AccountAttributeNotValid(String attributeName) {
        super(String.format("value of %s, is null or empty, not valid", attributeName), ERROR_CODE_NOT_VALID);
    }

    public AccountAttributeNotValid(int argumentExpected, int argumentreceived) {
        super(String.format("expected %o arguments, but received %o a, in %s", argumentExpected, argumentreceived), ERROR_CODE_ATTRIBUTE);
    }
}
