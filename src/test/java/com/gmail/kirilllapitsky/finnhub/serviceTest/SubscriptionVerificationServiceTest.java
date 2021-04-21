package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.MailNotificationsSender;
import com.gmail.kirilllapitsky.finnhub.service.SubscriptionVerificationService;
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
public class SubscriptionVerificationServiceTest {
    @InjectMocks
    private SubscriptionVerificationService subscriptionVerificationService;

    @Spy
    private SubscriptionRepository subscriptionRepository;

    @Spy
    private MailNotificationsSender mailNotificationsSender;

    private List<User> users;
    private List<Subscription> subscriptions;

    @Before
    public void initialize() {
        users = TestData.getUsers();
        subscriptions = TestData.getExpiringSubscriptions(users);
        subscriptions.get(0).setShouldBeRenew(true);
        subscriptions.get(0).setRenewLevel(Role.SENIOR);
        subscriptions.get(1).setShouldBeRenew(true);
        subscriptions.get(1).setRenewLevel(Role.SENIOR);
    }

    @Test
    public void shouldPassUsersWithEndedSubscriptionToNotify() {
        List<Subscription> shouldNotBeRenewed = subscriptions.stream().filter(subscription -> subscription.getEndDate().equals(LocalDate.now()) && !subscription.getShouldBeRenew()).collect(Collectors.toList());
        List<Subscription> expiringSubscriptions = subscriptionRepository.findAllByEndDate(LocalDate.now().minusDays(1));
        Mockito.when(subscriptionRepository.findAllByEndDateAndShouldBeRenew(LocalDate.now().minusDays(1), false)).thenReturn(shouldNotBeRenewed);
        Mockito.when(subscriptionRepository.findAllByEndDate(LocalDate.now().minusDays(1))).thenReturn(expiringSubscriptions);
        List<String> usersShouldBeNotifiedEmails = shouldNotBeRenewed
                .stream()
                .map(subscription -> subscription.getUser().getEmail())
                .collect(Collectors.toList());
        Mockito.doNothing().when(mailNotificationsSender).notifyUsers(any(), any());
        expiringSubscriptions = expiringSubscriptions
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

        subscriptionVerificationService.subscriptionVerification();
        Mockito.verify(subscriptionRepository).saveAll(expiringSubscriptions);
        Mockito.verify(mailNotificationsSender, Mockito.times(1)).notifyUsers(usersShouldBeNotifiedEmails, "Your subscription ended.");
    }
}
