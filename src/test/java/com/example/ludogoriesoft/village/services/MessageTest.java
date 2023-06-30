package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Message;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class MessageTest {

    @Test
    void testValidation() {
        // Create a Message instance with invalid data
        Message message = new Message();
        message.setUserName("A"); // Invalid length
        message.setEmail("invalid_email"); // Invalid email format
        message.setUserMessage(""); // Blank field

        // Validate the Message instance
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validate(message);

        // Check if any validation violations are present
        assertThat(violations).isNotEmpty();
        assertThat(violations).hasSize(3);

        // Check the violation messages
        assertThat(violations).extracting("message").contains(
                "Name should be at least than 2 characters long!",
                "Please enter a valid email address!"
        );
    }
}
