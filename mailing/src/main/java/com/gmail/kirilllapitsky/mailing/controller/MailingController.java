package com.gmail.kirilllapitsky.mailing.controller;

import com.gmail.kirilllapitsky.mailing.entity.Message;
import com.gmail.kirilllapitsky.mailing.service.MailNotificationsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mailing")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailingController {
    private final MailNotificationsSender mailNotificationsSender;

    @PostMapping
    public void mailing(@RequestBody String message) {
        mailNotificationsSender.mailing(message);
    }

    @PostMapping("/sendNotification")
    public void sendNotification(@RequestBody Message message) {
        mailNotificationsSender.sendNotification(message.getEmail(), message.getMessage());
    }
}
