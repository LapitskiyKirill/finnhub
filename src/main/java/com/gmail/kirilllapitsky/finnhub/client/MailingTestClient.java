package com.gmail.kirilllapitsky.finnhub.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "MailingTestClient", url = "${mailing.path}")
public interface MailingTestClient {
    @PostMapping(value = "/testing/verify")
    void verify();

    @PostMapping(value = "/testing/notify")
    void notifyUsers();
}
