package com.example.ludogorieSoft.village.exeptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


public record ApiVillageNotFound(String message,
                               Throwable throwable,
                               org.springframework.http.HttpStatus httpStatus,
                               ZonedDateTime timestamp) {


}
