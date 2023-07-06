package com.example.ludogorieSoft.village.services;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.logging.Logger;

class EmailSenderServiceTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private EmailSenderService emailSenderService;
    @Mock
    private Logger logger;
    @Value("${spring.mail.username}")
    private String recipientEmail;

    @Value("${spring.mail.password}")
    private String recipientPassword;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


}
