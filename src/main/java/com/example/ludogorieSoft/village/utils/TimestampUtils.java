package com.example.ludogorieSoft.village.utils;

import java.time.LocalDateTime;
public class TimestampUtils {
    private TimestampUtils(){}
    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

}
