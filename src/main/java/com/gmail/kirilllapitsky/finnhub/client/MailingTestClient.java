package com.gmail.kirilllapitsky.finnhub.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MailingTestClient", url = "${mailing.path}")
public interface MailingTestClient {
    @PostMapping(value = "/verify")
    void verify();

    @PostMapping(value = "/notify")
    void notifyUsers();
}
