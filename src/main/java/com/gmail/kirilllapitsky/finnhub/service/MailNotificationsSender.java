package com.gmail.kirilllapitsky.finnhub.service;


import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailNotificationsSender {
    private final JavaMailSender mailSender;
    @Value("$spring.mail.username")
    String projectMail;

    @Async
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