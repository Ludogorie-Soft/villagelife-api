package com.example.ludogorieSoft.village.exeptions;

public class UsernamePasswordException extends RuntimeException{
    public UsernamePasswordException(String message) {
        super(message);
    }
}