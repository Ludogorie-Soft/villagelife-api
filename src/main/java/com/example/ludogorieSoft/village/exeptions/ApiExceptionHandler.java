package com.example.ludogorieSoft.village.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {com.example.ludogorieSoft.village.exeptions.ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(com.example.ludogorieSoft.village.exeptions.ApiRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        com.example.ludogorieSoft.village.exeptions.ApiException apiException = new com.example.ludogorieSoft.village.exeptions.ApiException(
                e.getMessage(),
                e.getCause(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
}
