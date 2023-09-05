package com.example.ludogorieSoft.village.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;


import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(recipientEmail, mailSenderImpl.getUsername());
        assertEquals(recipientPassword, mailSenderImpl.getPassword());

        Properties configuredProperties = mailSenderImpl.getJavaMailProperties();
        assertEquals("true", configuredProperties.getProperty("mail.smtp.auth"));
        assertEquals("true", configuredProperties.getProperty("mail.smtp.starttls.enable"));
    }
    @Test
    void testAddTableRow() {
        StringBuilder emailBody = new StringBuilder();
        EmailSenderService emailSenderService = new EmailSenderService();
        emailSenderService.addTableRow(emailBody, "Title", "Info");
        String expectedEmailBody = "<tr><td style=\"border: 1px solid #CCCCCC; padding: 5px; width: 30%;\">Title</td><td style=\"border: 1px solid #CCCCCC; padding: 5px;\">Info</td></tr>";
        assertEquals(expectedEmailBody, emailBody.toString());
    }
}
