package com.example.ludogorieSoft.village.utils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
public class TimestampUtils {
    public static LocalDateTime getCurrentTimestamp() {
        return LocalDateTime.now();
    }

}
