package com.gmail.kirilllapitsky.finnhub.service;

import com.auth0.jwt.algorithms.Algorithm;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static com.auth0.jwt.JWT.require;
import static com.gmail.kirilllapitsky.finnhub.constants.SecurityConstants.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void changePassword(HttpServletRequest request, String newPassword) throws ApiException {
        String username = require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(request.getHeader(HEADER).replace(TOKEN_PREFIX, ""))
                .getSubject();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("Invalid token."));
        if (!user.isAccountNonLocked())
            throw new ApiException("Your account is locked");
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
