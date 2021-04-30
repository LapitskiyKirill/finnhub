package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.client.MailingTestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mailingTest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailingTestController {
    private final MailingTestClient mailingTestClient;

    @PostMapping(value = "/verify")
    void verify() {
        mailingTestClient.verify();
    }

    @PostMapping(value = "/notify")
    void notifyUsers() {
        mailingTestClient.notifyUsers();
    }
}
