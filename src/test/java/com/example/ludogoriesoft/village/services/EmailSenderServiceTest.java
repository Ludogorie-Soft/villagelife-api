package com.example.ludogorieSoft.village.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;


import java.util.Properties;

class EmailSenderServiceTest {

    @InjectMocks
    EmailSenderService emailSenderService;

    @Value("${spring.mail.username}")
    private String recipientEmail;

    @Value("${spring.mail.password}")
    private String recipientPassword;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConfigureMailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost("smtp.example.com");
        mailSenderImpl.setPort(25);

        mailSenderImpl.setUsername(recipientEmail);
        mailSenderImpl.setPassword(recipientPassword);

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.starttls.enable", "true");

        mailSenderImpl.setJavaMailProperties(javaMailProperties);

        emailSenderService.configureMailSender();

        Assertions.assertEquals(recipientEmail, mailSenderImpl.getUsername());
        Assertions.assertEquals(recipientPassword, mailSenderImpl.getPassword());

        Properties configuredProperties = mailSenderImpl.getJavaMailProperties();
        Assertions.assertEquals("true", configuredProperties.getProperty("mail.smtp.auth"));
        Assertions.assertEquals("true", configuredProperties.getProperty("mail.smtp.starttls.enable"));
    }
}
