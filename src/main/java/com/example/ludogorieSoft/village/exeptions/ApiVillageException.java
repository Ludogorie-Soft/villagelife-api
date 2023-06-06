package com.example.ludogorieSoft.village.exeptions;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class ApiVillageException {
    private final String message;
    private final Throwable throwable;
    private final org.springframework.http.HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ApiVillageException(String message,
                               Throwable throwable,
                               org.springframework.http.HttpStatus httpStatus,
                               ZonedDateTime timestamp) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

}
