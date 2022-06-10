package com.thief.controller.api.dto.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AccountUpdateDto {

    @NotBlank
    @NotNull
    private String name;

    @NotBlank
    @NotNull
    private String surname;
}
