package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.client.MailingClient;
import com.gmail.kirilllapitsky.finnhub.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MailingService {
    private final MailingClient mailingClient;

    public void mailing(String message) {
        mailingClient.mailing(message);
    }

    public void mailing(Message message) {
        mailingClient.sendNotification(message);
    }
}
