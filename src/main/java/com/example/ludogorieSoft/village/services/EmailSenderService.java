package com.example.ludogorieSoft.village.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String recipientEmail;

    @Value("${spring.mail.password}")
    private String recipientPassword;
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderService.class);
    public void sendEmail(String fromEmail, String body, String subject) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo("info@villagelife.bg");
            helper.setFrom(fromEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("An error occurred while sending an email", e);
        }
    }

    @PostConstruct
    public void configureMailSender() {
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            mailSenderImpl.setUsername(recipientEmail);
            mailSenderImpl.setPassword(recipientPassword);

            Properties mailProperties = mailSenderImpl.getJavaMailProperties();
            mailProperties.put("mail.smtp.auth", "true");
            mailProperties.put("mail.smtp.starttls.enable", "true");
        }
    }
    public void addTableRow(StringBuilder emailBody, String title, String info) {
        emailBody.append("<tr><td style=\"border: 1px solid #CCCCCC; padding: 5px; width: 30%;\">").append(title).append("</td><td style=\"border: 1px solid #CCCCCC; padding: 5px;\">").append(info).append("</td></tr>");
    }
}
