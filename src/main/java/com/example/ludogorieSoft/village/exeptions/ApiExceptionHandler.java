package com.example.ludogorieSoft.village.exeptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler  {
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<String> handleApiRequestException(ApiRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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
    @ExceptionHandler(NoConsentException.class)
    public ResponseEntity<String> handleNoConsentException(NoConsentException ex){
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
