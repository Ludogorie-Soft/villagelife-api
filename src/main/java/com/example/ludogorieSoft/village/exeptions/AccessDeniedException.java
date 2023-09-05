package com.example.ludogorieSoft.village.exeptions;


public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message){
        super(message) ;
    }
}
