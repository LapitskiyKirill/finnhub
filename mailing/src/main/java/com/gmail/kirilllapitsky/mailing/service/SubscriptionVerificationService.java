package com.gmail.kirilllapitsky.mailing.service;

import com.gmail.kirilllapitsky.mailing.Role;
import com.gmail.kirilllapitsky.mailing.entity.Subscription;
import com.gmail.kirilllapitsky.mailing.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.kirilllapitsky.mailing.util.UsersMapper.subscriptionToUserEmailMapper;

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
        List<Subscription> subscriptionsToUpdate = subscriptions.stream()
                .map(this::verifySubscription)
                .collect(Collectors.toList());
        subscriptionRepository.saveAll(subscriptionsToUpdate);

        List<String> usersShouldBeNotified = subscriptionToUserEmailMapper(expiredSubscriptions);
        mailNotificationsSender.notifyUsers(
                usersShouldBeNotified,
                "Your subscription ended."
        );
    }

    private Subscription verifySubscription(Subscription subscription) {
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
        return subscription;
    }
}

