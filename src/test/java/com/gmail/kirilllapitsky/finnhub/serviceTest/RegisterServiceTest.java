package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.dto.RegisterRequest;
import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.repository.SubscriptionRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.service.RegisterService;
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

import static org.junit.Assert.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegisterServiceTest {
    @InjectMocks
    private RegisterService registerService;

    @Spy
    private UserRepository userRepository;

    @Spy
    private SubscriptionRepository subscriptionRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    private RegisterRequest registerRequest;
    private Subscription subscription;
    private User user;

    @Before
    public void initialize() {
        registerRequest = TestData.getRegisterRequest();
        subscription = TestData.getSubscription();
        user = TestData.getUserByRegisterRequest(registerRequest);
        subscription.setUser(user);
    }

    @Test
    public void shouldRegister() throws ApiException {
        Mockito.when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(registerRequest.getPassword());

        registerService.signUp(registerRequest);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(subscriptionRepository, Mockito.times(1)).save(subscription);
    }

    @Test
    public void shouldNotRegisterWhenUsernameOrEmailExists() {
        Mockito.when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(registerRequest.getPassword());

        assertThrows("User with this email already exists.", ApiException.class, () -> registerService.signUp(registerRequest));
    }
}
