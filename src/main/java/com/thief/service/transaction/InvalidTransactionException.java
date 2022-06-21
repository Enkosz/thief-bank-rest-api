package com.thief.service.transaction;

public class InvalidTransactionException extends ThiefException {

    public static final String INVALID_TRANSACTION_CODE = "INVALID_TRANSACTION";
    public static final String TRANSACTION_NOT_FOUND_CODE = "TRANSACTION_NOT_FOUND";
    public static final String INVALID_REVERT_CODE = "INVALID_REVERT";

    public InvalidTransactionException(String message, String code) {
        super(message, code);
    }
}
