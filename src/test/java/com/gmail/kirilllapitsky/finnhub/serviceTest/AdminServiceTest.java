package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.client.MailingClient;
import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.AdminService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;
    @Spy
    private UserRepository userRepository;
    @Spy
    private SubscriptionRepository subscriptionRepository;
    @Spy
    private PasswordEncoder passwordEncoder;
    @Spy
    private MailingClient mailingService;

    private RegisterRequest registerRequest;
    private Subscription subscription;
    private User user;

    @Before
    public void initialize() {
        registerRequest = TestData.getRegisterRequest();
        subscription = TestData.getSubscription();
        user = TestData.getUserByRegisterRequest(registerRequest);
        subscription.setUser(user);
        subscription.setRole(Role.ADMIN);
    }

    @Test
    public void shouldCreateAdminAccount() throws ApiException {
        Mockito.when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(registerRequest.getPassword());

        adminService.createAdminAccount(registerRequest);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(subscription);
    }

    @Test
    public void shouldBlockUser() throws NoSuchEntityException {
        user.setAccountNonLocked(true);
        User shouldSaveUser = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .role(user.getRole())
                .isAccountNonLocked(user.isAccountNonLocked())
                .isEnabled(user.isEnabled())
                .isCredentialsNonExpired(user.isCredentialsNonExpired())
                .isAccountNonExpired(user.isAccountNonExpired())
                .build();
        shouldSaveUser.setAccountNonLocked(false);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(mailingService).sendNotification(any());

        adminService.blockUser(user.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailingService, Mockito.times(1)).sendNotification(any());
    }

    @Test
    public void shouldUnBlockUser() throws NoSuchEntityException {
        user.setAccountNonLocked(false);
        User shouldSaveUser = User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .role(user.getRole())
                .isAccountNonLocked(user.isAccountNonLocked())
                .isEnabled(user.isEnabled())
                .isCredentialsNonExpired(user.isCredentialsNonExpired())
                .isAccountNonExpired(user.isAccountNonExpired())
                .build();
        shouldSaveUser.setAccountNonLocked(true);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.doNothing().when(mailingService).sendNotification(any());

        adminService.blockUser(user.getUsername());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailingService, Mockito.times(1)).sendNotification(any());
    }

    @Test
    public void shouldSetSubscription() throws NoSuchEntityException {
        subscription.setUser(user);
        subscription.setRole(Role.BEGINNER);
        Mockito.when(subscriptionRepository.findById(user.getId())).thenReturn(Optional.of(subscription));
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        adminService.setSubscription(user.getUsername(), Role.BEGINNER);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(subscription);
        Mockito.verify(mailingService, Mockito.times(1)).sendNotification(any());
    }

    @Test
    public void shouldDeleteSubscription() throws NoSuchEntityException {
        user.setId(1L);
        subscription.setUser(user);
        Mockito.when(subscriptionRepository.findById(user.getId())).thenReturn(Optional.of(subscription));
        Subscription shouldSaveSubscription = Subscription.builder()
                .user(user)
                .shouldBeRenew(false)
                .role(Role.GUEST)
                .endDate(null)
                .renewLevel(null)
                .build();
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        adminService.deleteSubscription(user.getUsername());
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(shouldSaveSubscription);
        Mockito.verify(mailingService, Mockito.times(1)).sendNotification(any());
    }

}
