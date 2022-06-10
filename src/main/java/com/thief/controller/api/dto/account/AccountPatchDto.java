package com.thief.controller.api.dto.account;

import com.thief.util.OneOf;
import lombok.Data;

@Data
@OneOf(fields = {"name","surname"}, message="Either name or surname must be set")
public class AccountPatchDto {

    private String name;
    private String surname;
}
