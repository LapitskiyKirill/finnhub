package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionVerificationService {
    private final SubscriptionRepository subscriptionRepository;
    private final MailNotificationsSender mailNotificationsSender;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    @Transactional
    public void subscriptionVerification() {
        List<Subscription> expiredSubscriptions = subscriptionRepository.findAllByEndDateAndShouldBeRenew(LocalDate.now().minusDays(1), false);
        List<Subscription> subscriptions = subscriptionRepository.findAllByEndDate(LocalDate.now().minusDays(1));
        subscriptions =
                subscriptions
                        .stream()
                        .peek(subscription -> {
                            if (subscription.getShouldBeRenew()) {
                                subscription.setEndDate(LocalDate.now().plusDays(30));
                                subscription.setRole(subscription.getRenewLevel());
                                subscription.setRenewLevel(null);
                                subscription.setShouldBeRenew(false);
                            } else {
                                subscription.setEndDate(null);
                                subscription.setStartDate(LocalDate.now());
                                subscription.setRole(Role.GUEST);
                            }
                        })
                        .collect(Collectors.toList());
        subscriptionRepository.saveAll(subscriptions);
        List<User> usersShouldBeNotified = expiredSubscriptions
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toList());
        mailNotificationsSender.notifyUsers(
                usersShouldBeNotified
                        .stream()
                        .map(User::getEmail)
                        .collect(Collectors.toList()),
                "Your subscription ended."
        );
    }
}

