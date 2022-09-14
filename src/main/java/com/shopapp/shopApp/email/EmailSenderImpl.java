package com.shopapp.shopApp.email;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@AllArgsConstructor
public class EmailSenderImpl implements EmailSender{

    private final JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendEmail(String receiver, String emailBody) {

        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper msgHelper = new MimeMessageHelper(msg, "utf-8");

            msgHelper.setText(emailBody, true);
            msgHelper.setTo(receiver);
            msgHelper.setSubject("Confirm your email address");
            msgHelper.setFrom("filip7243@gmail.com");

            javaMailSender.send(msg);

        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new IllegalStateException("Failed to send email");
        }
    }
}
