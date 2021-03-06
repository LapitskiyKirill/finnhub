package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegisterService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(RegisterRequest request) throws ApiException {
        if (userRepository.findByUsername(request.getUsername()).isPresent())
            throw new ApiException("User with this username already exists.");
        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new ApiException("User with this email already exists.");
        User user = User.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .email(request.getEmail())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
        userRepository.save(user);
        Subscription subscription = Subscription
                .builder()
                .user(user)
                .role(Role.GUEST)
                .shouldBeRenew(false)
                .build();
        subscriptionRepository.save(subscription);
    }
}
