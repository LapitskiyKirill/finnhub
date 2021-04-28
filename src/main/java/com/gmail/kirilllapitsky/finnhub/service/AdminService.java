package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.client.MailingClient;
import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.entity.Message;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailingClient mailingService;
    @Value("${mailing.message.block}")
    private String block;
    @Value("${mailing.message.unblock}")
    private String unblock;
    @Value("${mailing.message.subscription.renew}")
    private String renew;
    @Value("${mailing.message.subscription.delete}")
    private String delete;

    public void createAdminAccount(RegisterRequest request) throws ApiException {
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
                .role(Role.ADMIN)
                .shouldBeRenew(false)
                .build();
        subscriptionRepository.save(subscription);
    }

    public void blockUser(String username) throws NoSuchEntityException {
        User user = findUserByUsername(username);
        user.setAccountNonLocked(false);
        userRepository.save(user);
        mailingService.sendNotification(new Message(block, user.getEmail()));
    }

    public void unBlockUser(String username) throws NoSuchEntityException {
        User user = findUserByUsername(username);
        user.setAccountNonLocked(true);
        userRepository.save(user);
        mailingService.sendNotification(new Message(unblock, user.getEmail()));
    }

    public void setSubscription(String username, Role role) throws NoSuchEntityException {
        User user = findUserByUsername(username);
        Subscription subscription = Subscription
                .builder()
                .user(user)
                .role(role)
                .shouldBeRenew(false)
                .build();
        subscriptionRepository.save(subscription);
        mailingService.sendNotification(new Message(renew, user.getEmail()));

    }

    public void deleteSubscription(String username) throws NoSuchEntityException {
        User user = findUserByUsername(username);
        Subscription subscription = subscriptionRepository.findById(user.getId()).get();
        subscription.setRenewLevel(null);
        subscription.setShouldBeRenew(false);
        subscription.setEndDate(null);
        subscription.setRole(Role.GUEST);
        subscriptionRepository.save(subscription);
        mailingService.sendNotification(new Message(delete, user.getEmail()));
    }

    public void mailing(String message) {
        mailingService.mailing(message);
    }

    private User findUserByUsername(String username) throws NoSuchEntityException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchEntityException(String.format("No user with username %s.", username)));
    }
}
