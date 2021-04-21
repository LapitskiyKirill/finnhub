package com.gmail.kirilllapitsky.finnhub.service;


import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class MailNotificationsSender {
    @Autowired
    private JavaMailSender mailSender;
    @Value("$spring.mail.username")
    private String projectMail;

    public void notifyUsers(List<String> emails, String message) {
        emails.forEach(email -> {
            MimeMessagePreparator mimeMessagePreparator =
                    mimeMessage -> {
                        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                        messageHelper.setFrom(projectMail);
                        messageHelper.setTo(email);
                        messageHelper.setSubject("FinnhubProject notification.");
                        messageHelper.setText(message);
                    };
            try {
                mailSender.send(mimeMessagePreparator);
            } catch (MailException e) {
                try {
                    e.printStackTrace();
                    throw new ApiException("Can`t send message.");
                } catch (ApiException apiException) {
                    apiException.printStackTrace();
                }
            }
        });
    }
}