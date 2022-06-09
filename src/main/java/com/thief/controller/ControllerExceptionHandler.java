package com.thief.controller;

import com.thief.controller.dto.ApiResponse;
import com.thief.service.transaction.ThiefException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ThiefException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> handle(ThiefException exception){
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(exception.getCode());
        apiResponse.setMessage(exception.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
