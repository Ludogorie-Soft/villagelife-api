package com.example.ludogorieSoft.village.exeptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(com.example.ludogorieSoft.village.exeptions.ApiRequestException e){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ApiVillageNotFound apiException = new ApiVillageNotFound(
                e.getMessage(),
                e.getCause(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<String> handleJWTException(JwtException e) {
        String errorMessage;
        if (e.getMessage().contains("JWT expired")) {
            errorMessage = "JWT token has expired.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        } else {
            errorMessage = "Invalid JWT token: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
        }
    }

//    @ExceptionHandler(DataIntegrityViolationException.class)
//    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
//        String errorMessage = "Duplicate entry error";
//
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//    }
}
