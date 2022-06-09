package com.thief.service.transaction;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ThiefException extends RuntimeException {

    protected final String code;

    public ThiefException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ThiefException(String code) {
        this.code = code;
    }
}
