package com.gmail.kirilllapitsky.finnhub.service;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@NoArgsConstructor
public class MailNotificationsSender {
    @Autowired
    private JavaMailSender mailSender;
    @Value("$spring.mail.username")
    private String projectMail;

    public void notifyUsers(List<String> emails, String message) {
        emails.forEach(email -> {
            MimeMessagePreparator mimeMessagePreparator = getMessage(email, message);
            try {
                mailSender.send(mimeMessagePreparator);
            } catch (MailException e) {
                log.error(e.getMessage());
            }
        });
    }

    private MimeMessagePreparator getMessage(String email, String message) {
        return mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(projectMail);
            messageHelper.setTo(email);
            messageHelper.setSubject("FinnhubProject notification.");
            messageHelper.setText(message);
        };
    }
}