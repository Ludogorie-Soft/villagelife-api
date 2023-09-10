package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.utils.ValidationUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilsTest {

    @Test
    void testIsValidNameValid() {
        String validName = "Петър Петров";
        assertTrue(ValidationUtils.isValidName(validName));
    }

    @Test
    void testIsValidNameInvalid() {
        String invalidName = "John123";
        assertFalse(ValidationUtils.isValidName(invalidName));
    }

    @Test
    void testIsValidNumberValid() {
        String validNumber = "12345";
        assertTrue(ValidationUtils.isValidNumber(validNumber));
    }

    @Test
    void testIsValidNumberInvalid() {
        String invalidNumber = "abc";
        assertFalse(ValidationUtils.isValidNumber(invalidNumber));
    }
}
