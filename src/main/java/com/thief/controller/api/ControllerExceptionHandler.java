package com.thief.controller.api;

import com.thief.controller.api.dto.ApiResponse;
import com.thief.service.transaction.ThiefException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ThiefException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> handleThiefException(ThiefException exception){
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode(exception.getCode());
        apiResponse.setMessage(exception.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ApiResponse<?>> handleConstraintViolationException(ConstraintViolationException e) {
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode("VALIDATION_ERROR");
        apiResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<ApiResponse<?>> handleConstraintViolationException(MethodArgumentNotValidException e) {
        ApiResponse<?> apiResponse = new ApiResponse<>();
        String message = e.getBindingResult().getAllErrors()
                        .get(0).getDefaultMessage();

        apiResponse.setCode("VALIDATION_ERROR");
        apiResponse.setMessage(message);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        ApiResponse<?> apiResponse = new ApiResponse<>();

        apiResponse.setCode("UNKNOWN_ERROR");
        apiResponse.setMessage("Unknown error occurred");

        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
