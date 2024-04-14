package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.model.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE_TIME;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

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

        Message message = new Message(id, userName, email, userMessage, null);

        Assertions.assertEquals(id, message.getId());
        Assertions.assertEquals(userName, message.getUserName());
        Assertions.assertEquals(email, message.getEmail());
        Assertions.assertEquals(userMessage, message.getUserMessage());
    }

    @Test
    void createMessage_IdGenerated_Success() {
        String userName = "John";
        String email = "john@example.com";
        String userMessage = "Hello";
        Message message = new Message(null, userName, email, userMessage, null);
        assertNotNull(message.getId() == null);
    }


    @Test
    void hasNoArgsConstructor() {
        Class<?> messageClass = Message.class;
        boolean hasNoArgsConstructor;
        try {
            messageClass.getDeclaredConstructor();
            hasNoArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(hasNoArgsConstructor);
    }

    @Test
    void hasAllArgsConstructor() {
        Class<?> messageClass = Message.class;
        boolean hasAllArgsConstructor;
        try {
            messageClass.getDeclaredConstructor(Long.class, String.class, String.class, String.class, LocalDateTime.class);
            hasAllArgsConstructor = true;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertTrue(hasAllArgsConstructor);
    }

    @Test
    void validateNotBlankUserName_ValidValue_NoValidationErrors() {
        Message message = new Message();
        message.setUserName("John");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userName");
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankUserName_NullValue_ValidationErrors() {
        Message message = new Message();
        message.setUserName(null);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userName");
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        Assertions.assertEquals("Name cannot be empty!", violation.getMessage());
    }

    @Test
    void validateNotBlankEmail_ValidValue_NoValidationErrors() {
        Message message = new Message();
        message.setEmail("john@example.com");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankEmail_NullValue_ValidationErrors() {
        Message message = new Message();
        message.setEmail(null);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        Assertions.assertEquals("Email cannot be empty!", violation.getMessage());
    }

    @Test
    void validateEmail_ValidValue_NoValidationErrors() {
        Message message = new Message();
        message.setEmail("john@example.com");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateEmail_InvalidValue_ValidationErrors() {
        Message message = new Message();
        message.setEmail("invalid-email");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "email");
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        Assertions.assertEquals("Please enter a valid email address!", violation.getMessage());
    }

    @Test
    void validateNotBlankUserMessage_ValidValue_NoValidationErrors() {
        Message message = new Message();
        message.setUserMessage("Hello");
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userMessage");
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateNotBlankUserMessage_NullValue_ValidationErrors() {
        Message message = new Message();
        message.setUserMessage(null);
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Message>> violations = validator.validateProperty(message, "userMessage");
        Assertions.assertEquals(1, violations.size());
        ConstraintViolation<Message> violation = violations.iterator().next();
        Assertions.assertEquals("must not be blank", violation.getMessage());
    }


}
