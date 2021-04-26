package com.gmail.kirilllapitsky.mailing.service;


import com.gmail.kirilllapitsky.mailing.entity.User;
import com.gmail.kirilllapitsky.mailing.repository.UserRepository;
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
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor
public class MailNotificationsSender {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
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

    public void sendNotification(String email, String message) {
        MimeMessagePreparator mimeMessagePreparator = getMessage(email, message);
        try {
            mailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            log.error(e.getMessage());
        }
    }

    public void mailing(String message) {
        List<String> emails = userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
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