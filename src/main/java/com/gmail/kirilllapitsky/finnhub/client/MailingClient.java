package com.gmail.kirilllapitsky.finnhub.client;

import com.gmail.kirilllapitsky.finnhub.entity.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MailingClient", url = "${mailing.path}")
public interface MailingClient {
    @PostMapping(value = "/mailing")
    void mailing(String message);

    @PostMapping(value = "/mailing/sendNotification")
    void sendNotification(Message message);
}
