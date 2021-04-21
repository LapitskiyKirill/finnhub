package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
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
public class SubscriptionNotificationService {
    private final SubscriptionRepository subscriptionRepository;
    private final MailNotificationsSender mailNotificationsSender;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    @Transactional
    public void subscriptionWeekNotification() {
        List<Subscription> subscriptionsShouldExpiredInWeek = subscriptionRepository.findAllByEndDate(LocalDate.now().plusDays(7));
        List<User> usersShouldBeNotified = subscriptionsShouldExpiredInWeek
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toList());
        mailNotificationsSender.notifyUsers(
                usersShouldBeNotified
                        .stream()
                        .map(User::getEmail)
                        .collect(Collectors.toList())
                , "Your subscription will end " + LocalDate.now().toString()
        );
    }
}
