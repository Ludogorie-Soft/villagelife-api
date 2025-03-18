package com.example.ludogorieSoft.village.services;

import com.example.ludogorieSoft.village.dtos.VerificationTokenDTO;
import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import com.example.ludogorieSoft.village.model.AlternativeUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
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

    @Value("${host.url}")
    private String hostURL;

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
        logger.info(">>>>>>>>>>>>>Verification email sent to " + user.getEmail());
        try {
            String body = createVerificationEmailBody(token, user);
            sendToEmail(user.getEmail(), body, "Активационен код");
            logger.info("Verification email sent to " + user.getEmail());
        } catch (MessagingException e) {
            logger.error("An error occurred while sending a verification email", e);
        }
    }

    public void sendResetPasswordEmail(VerificationTokenDTO token, AlternativeUser user) {
        try {
            String body = createResetPasswordEmailBody(token, user);//мейлът праща линк с данните за токена
            sendToEmail(user.getEmail(), body, "Смяна на парола");
            logger.info("Reset password email sent to " + user.getEmail());
        } catch (MessagingException e) {
            logger.error("An error occurred while sending a verification email", e);
        }
    }

    private String createResetPasswordEmailBody(VerificationTokenDTO token, AlternativeUser user) {
        String fullName = user.getFullName();
        String email = user.getEmail();

        LocalDateTime expiryDate = token.getExpiryDate().minusMinutes(15);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm");
        String formattedDate = expiryDate.format(formatter);
        String resetPasswordLink = hostURL + "/auth/reset-password-form?token=" + token.getToken() + "&userId=" + user.getId();
        return "<div style='text-align: center;'>"
                + "<a href='https://villagelife.bg'>"
                + "<img src='cid:logoImage' style='width: 200px;' alt='Site Logo'/>"
                + "</a>"
                + "<h2>Здравейте " + fullName + ",</h2>"
                + "<p>Получавате този имейл, защото на " + formattedDate + " беше направен опит за промяна на паролата на вашия профил с имейл: <strong>" + email + "</strong>. Ако не сте извършвали такъв опит, игнорирайте имейла.</p>"
                + "<p>Натиснете <a href='" + resetPasswordLink + "'>ТУК</a>, за да промените паролата си.</p>"
                + "<p>Пожелаваме Ви успех,<p/>"
                + "<p>Екип на <a href='https://villagelife.bg'>villagelife.bg</a></p>"
                + "</div>";
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
                + "<p>За да активирате профила си, моля използвайте <a href='"+hostURL+"/auth/verify-verification-token'>ТОЗИ</a> линк</p>"
                + "<p>Пожелаваме Ви успех,<p/>"
                + "<p>Екип на <a href='https://villagelife.bg'>villagelife.bg</a></p>"
                + "</div>";
    }

    private void sendToEmail(String email, String body, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setFrom(recipientEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

//        FileSystemResource logo = new FileSystemResource("src/main/resources/static/images/logo.png");
//        helper.addInline("logoImage", logo); // Use "cid:logoImage" in the HTML to refer to this image

        ClassPathResource resource = new ClassPathResource("static/images/logo.png");
        InputStreamSource logoSource = new InputStreamSource() {
            @Override
            public InputStream getInputStream() throws IOException {
                return resource.getInputStream();
            }
        };
        helper.addInline("logoImage", logoSource, "image/png");

        try {
            mailSender.send(message);
        } catch (MailSendException ex) {
            throw new ApiRequestException(ex.getMessage());
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
