package com.thief.controller.api.dto.account;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateAccountDto {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String surname;
}
