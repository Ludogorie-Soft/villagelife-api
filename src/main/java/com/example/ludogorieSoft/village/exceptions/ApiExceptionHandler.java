package com.example.ludogorieSoft.village.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(com.example.ludogorieSoft.village.exceptions.ApiRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiVillageNotFound apiException = new ApiVillageNotFound(
                e.getMessage(),
                e.getCause(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
}
