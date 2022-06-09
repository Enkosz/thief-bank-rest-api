package com.thief.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class ApiResponse<T> {

    private String code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private String message;
}
