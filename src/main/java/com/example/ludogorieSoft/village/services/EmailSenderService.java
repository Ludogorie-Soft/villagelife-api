package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VerificationTokenDTO;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void sendVerificationToken(VerificationTokenDTO token, AlternativeUser user) {
        try {
            String body = createVerificationEmailBody(token, user);
            sendVerificationEmail(user.getEmail(), body);
            logger.info("Verification email sent to " + user.getEmail());
        } catch (MessagingException e) {
            logger.error("An error occurred while sending a verification email", e);
        }
    }

    private String createVerificationEmailBody(VerificationTokenDTO token, AlternativeUser user) {
        String fullName = user.getFullName();
        String email = user.getEmail();

        LocalDateTime expiryDate = token.getExpiryDate().minusMinutes(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm");
        String formattedDate = expiryDate.format(formatter);

        return "<div style='text-align: center;'>"
                + "<a href='https://villagelife.bg'>"
                + "<img src='cid:logoImage' style='width: 200px;' alt='Site Logo'/>"
                + "</a>"
                + "<h2>Здравейте " + fullName + ",</h2>"
                + "<p>Получавате този имейл, защото на " + formattedDate + " беше извършена регистрация с вашия имейл: <strong>" + email + "</strong>. Ако не сте извършвали регистрация, игнорирайте имейла.</p>"
                + "<p>Вашият код за активация на профила Ви е: <strong>" + token.getToken() + "</strong></p>"
                + "<p>Използвайте този код, за да активирате Вашия профил във <a href='https://villagelife.bg'>villagelife.bg</a>.</p>"
                + "<p>Пожелаваме Ви успех,<p/>"
                + "<p>Екип на <a href='https://villagelife.bg'>villagelife.bg</a></p>"
                + "</div>";
    }

    private void sendVerificationEmail(String email, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Активационен код");
        helper.setText(body, true);

        FileSystemResource logo = new FileSystemResource("src/main/resources/static/images/logo.png");
        helper.addInline("logoImage", logo); // Use "cid:logoImage" in the HTML to refer to this image

        mailSender.send(message);
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
