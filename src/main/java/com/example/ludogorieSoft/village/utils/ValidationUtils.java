package com.example.ludogorieSoft.village.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ValidationUtils {

    public static boolean isValidName(String name) {
        String regex = "^[^0-9]*$";
        return name.matches(regex);
    }

    public static boolean isValidNumber(String number) {
        String regex = "^[0-9]+$";
        return number.matches(regex);
    }
}
