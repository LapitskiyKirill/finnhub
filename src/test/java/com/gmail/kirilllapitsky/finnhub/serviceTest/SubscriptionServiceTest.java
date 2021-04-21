package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.SubscriptionService;
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
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubscriptionServiceTest {
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Spy
    private SubscriptionRepository subscriptionRepository;

    private User user;
    private Subscription subscription;

    @Before
    public void initialize() {
        user = TestData.getUser();
        subscription = TestData.getSubscription();
        subscription.setRole(Role.BEGINNER);
        subscription.setEndDate(LocalDate.now());
        subscription.setId(1L);
        user.setId(1L);
        subscription.setUser(user);
    }

    @Test
    public void shouldRenewSubscriptionIfNewLevelEquals() {
        Mockito.when(subscriptionRepository.findById(user.getId())).thenReturn(Optional.of(subscription));
        Subscription expectedSubscription = Subscription
                .builder()
                .id(subscription.getId())
                .role(subscription.getRole())
                .shouldBeRenew(false)
                .endDate(subscription.getEndDate().plusDays(31))
                .startDate(subscription.getStartDate())
                .renewLevel(null)
                .user(user)
                .build();

        subscriptionService.setSubscription(user, Role.BEGINNER);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(expectedSubscription);
    }

    @Test
    public void shouldRenewSubscriptionIfNewLevelHigher() {
        Mockito.when(subscriptionRepository.findById(user.getId())).thenReturn(Optional.of(subscription));
        Subscription expectedSubscription = Subscription
                .builder()
                .id(subscription.getId())
                .role(Role.MIDDLE)
                .shouldBeRenew(false)
                .endDate(LocalDate.now().plusDays(31))
                .startDate(LocalDate.now())
                .renewLevel(null)
                .user(user)
                .build();

        subscriptionService.setSubscription(user, Role.MIDDLE);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(expectedSubscription);
    }

    @Test
    public void shouldRenewSubscriptionIfNewLevelLower() {
        subscription.setRole(Role.MIDDLE);
        Mockito.when(subscriptionRepository.findById(user.getId())).thenReturn(Optional.of(subscription));
        Subscription expectedSubscription = Subscription
                .builder()
                .id(subscription.getId())
                .role(subscription.getRole())
                .shouldBeRenew(true)
                .endDate(subscription.getEndDate())
                .startDate(subscription.getStartDate())
                .renewLevel(Role.BEGINNER)
                .user(user)
                .build();

        subscriptionService.setSubscription(user, Role.BEGINNER);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(expectedSubscription);
    }

}
