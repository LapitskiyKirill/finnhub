package com.gmail.kirilllapitsky.mailing.service;

import com.gmail.kirilllapitsky.mailing.entity.Subscription;
import com.gmail.kirilllapitsky.mailing.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.gmail.kirilllapitsky.mailing.util.UsersMapper.subscriptionToUserEmailMapper;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubscriptionNotificationService {
    private final SubscriptionRepository subscriptionRepository;
    private final MailNotificationsSender mailNotificationsSender;

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    @Transactional
    public void subscriptionWeekNotification() {
        List<Subscription> subscriptionsShouldExpiredInWeek = subscriptionRepository.findAllByEndDate(LocalDate.now().plusDays(7));
        List<String> usersShouldBeNotified = subscriptionToUserEmailMapper(subscriptionsShouldExpiredInWeek);
        mailNotificationsSender.notifyUsers(
                usersShouldBeNotified,
                "Your subscription will end " + LocalDate.now().toString()
        );
    }
}
