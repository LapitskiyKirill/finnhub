package com.gmail.kirilllapitsky.finnhub.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    public static String SECRET;
    public static Long EXPIRATION_TIME;
    public static String TOKEN_PREFIX;
    public static String HEADER;

    public SecurityConstants(@Value("${jwt.token.secret}") String secret,
                             @Value("${jwt.token.expiration-time}") Long expirationTime,
                             @Value("${jwt.token.prefix}") String tokenPrefix,
                             @Value("${jwt.token.header}") String header
    ) {
        SECRET = secret;
        EXPIRATION_TIME = expirationTime;
        TOKEN_PREFIX = tokenPrefix;
        HEADER = header;
    }
}

