package com.gmail.kirilllapitsky.mailing.controller;

import com.gmail.kirilllapitsky.mailing.service.SubscriptionNotificationService;
import com.gmail.kirilllapitsky.mailing.service.SubscriptionVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestingController {
    private final SubscriptionVerificationService subscriptionVerificationService;
    private final SubscriptionNotificationService subscriptionNotificationService;

    @PostMapping("verify")
    public void verify() {
        subscriptionVerificationService.subscriptionVerification();
    }

    @PostMapping("notify")
    public void notifyUsers() {
        subscriptionNotificationService.subscriptionWeekNotification();
    }
}
