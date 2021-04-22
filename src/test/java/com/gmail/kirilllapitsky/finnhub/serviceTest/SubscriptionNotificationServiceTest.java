package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.service.MailNotificationsSender;
import com.gmail.kirilllapitsky.finnhub.service.SubscriptionNotificationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubscriptionNotificationServiceTest {
    @InjectMocks
    private SubscriptionNotificationService subscriptionNotificationService;

    @Spy
    private SubscriptionRepository subscriptionRepository;

    @Spy
    private MailNotificationsSender mailNotificationsSender;

    private List<User> users;
    private List<Subscription> subscriptions;

    @Before
    public void initialize() {
        users = TestData.getUsers();
        subscriptions = TestData.getSubscriptions(users);
        subscriptions.get(2).setEndDate(null);
        subscriptions.get(1).setEndDate(LocalDate.now().plusDays(2));
    }

    @Test
    public void shouldPassUsersWithEndingSubscriptionToNotify() {
        List<Subscription> repositorySubscriptions = subscriptions
                .stream()
                .filter(subscription -> subscription.getEndDate() != null && subscription.getEndDate().equals(LocalDate.now().plusDays(7)))
                .collect(Collectors.toList());
        Mockito.when(subscriptionRepository.findAllByEndDate(LocalDate.now().plusDays(7))).thenReturn(repositorySubscriptions);
        List<String> usersShouldBeNotifiedEmails = repositorySubscriptions
                .stream()
                .map(subscription -> subscription.getUser().getEmail())
                .collect(Collectors.toList());
        Mockito.doNothing().when(mailNotificationsSender).notifyUsers(any(), any());

        subscriptionNotificationService.subscriptionWeekNotification();
        Mockito.verify(mailNotificationsSender, Mockito.times(1)).notifyUsers(usersShouldBeNotifiedEmails, "Your subscription will end " + LocalDate.now().toString());
    }
}
