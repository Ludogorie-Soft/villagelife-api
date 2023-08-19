package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class MessageTest {

    @Test
    void testValidation() {
        Message message = new Message();
        message.setUserName("A");
        message.setEmail("invalid_email");
        message.setUserMessage("");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validate(message);

        assertThat(violations).isNotEmpty().hasSize(3);

        assertThat(violations).extracting("message").contains(
                "Name should be at least than 2 characters long!",
                "Please enter a valid email address!"
        );
    }

    @Test
    void createMessageValidDataSuccess() {
        Long id = 1L;
        String userName = "John";
        String email = "john@example.com";
        String userMessage = "Hello";

        Message message = new Message(id, userName, email, userMessage);

        Assertions.assertEquals(id, message.getId());
        Assertions.assertEquals(userName, message.getUserName());
        Assertions.assertEquals(email, message.getEmail());
        Assertions.assertEquals(userMessage, message.getUserMessage());
    }

    @Test
    void createMessage_IdGenerated_Success() {
        // Arrange
        String userName = "John";
        String email = "john@example.com";
        String userMessage = "Hello";

        // Act
        Message message = new Message(null, userName, email, userMessage);

        // Assert
        assertNotNull(message.getId() == null);
    }


    @Test
    void hasNoArgsConstructor() {
        // Arrange
        Class<?> messageClass = Message.class;
        boolean hasNoArgsConstructor = false;
        try {
            messageClass.getDeclaredConstructor();
            hasNoArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            // Ignore
        }

        // Assert
        assertTrue(hasNoArgsConstructor);
    }

    @Test
    void hasAllArgsConstructor() {
        // Arrange
        Class<?> messageClass = Message.class;
        boolean hasAllArgsConstructor = false;
        try {
            messageClass.getDeclaredConstructor(Long.class, String.class, String.class, String.class);
            hasAllArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            // Ignore
        }

        // Assert
        assertTrue(hasAllArgsConstructor);
    }

    @Test
    void validateNotBlankUserName_ValidValue_NoValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setUserName("John");

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userName");

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankUserName_NullValue_ValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setUserName(null);

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userName");

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        assertEquals("Name cannot be empty!", violation.getMessage());
    }

    @Test
    void validateNotBlankEmail_ValidValue_NoValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setEmail("john@example.com");

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankEmail_NullValue_ValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setEmail(null);

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        assertEquals("Email cannot be empty!", violation.getMessage());
    }

    @Test
    void validateEmail_ValidValue_NoValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setEmail("john@example.com");

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    void validateEmail_InvalidValue_ValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setEmail("invalid-email");

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        assertEquals("Please enter a valid email address!", violation.getMessage());
    }

    @Test
    void validateNotBlankUserMessage_ValidValue_NoValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setUserMessage("Hello");

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userMessage");

        // Assert
        assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankUserMessage_NullValue_ValidationErrors() {
        // Arrange
        Message message = new Message();
        message.setUserMessage(null);

        // Validate
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userMessage");

        // Assert
        assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        assertEquals("must not be blank", violation.getMessage());
    }


}
