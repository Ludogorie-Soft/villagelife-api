package com.example.ludogorieSoft.village.exeptions;


public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
