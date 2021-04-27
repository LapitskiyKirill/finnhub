package com.gmail.kirilllapitsky.fetching.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        String error = String.format("status: %d, reason: %s", response.status(), response.reason());
        log.error(error);
        return new Exception(error);
    }
}