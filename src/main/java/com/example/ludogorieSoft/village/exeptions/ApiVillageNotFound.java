package com.example.ludogoriesoft.village.exeptions;

import java.time.ZonedDateTime;


public record ApiVillageNotFound(String message,
                               Throwable throwable,
                               org.springframework.http.HttpStatus httpStatus,
                               ZonedDateTime timestamp) {


}
