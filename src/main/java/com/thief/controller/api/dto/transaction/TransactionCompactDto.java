package com.thief.controller.api.dto.transaction;

import lombok.Data;

import java.util.Date;

@Data
public class TransactionCompactDto {

    private String id;
    private Date date;
}
