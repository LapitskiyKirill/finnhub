package com.gmail.kirilllapitsky.finnhub.client;

import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        String error = String.format("status: %d, reason: %s", response.status(), response.reason());
        log.error(error);
        return new ApiException(error);
    }
}
