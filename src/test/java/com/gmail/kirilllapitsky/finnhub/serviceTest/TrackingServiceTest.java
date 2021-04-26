package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.TrackingService;
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

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TrackingServiceTest {
    @InjectMocks
    private TrackingService trackingService;
    @Spy
    private UserRepository userRepository;
    @Spy
    private CompanyRepository companyRepository;
    private User user;
    private Company company;

    @Before
    public void initialize() {
        user = TestData.getUser();
        user.setTrackingCompanies(new HashSet<>());
        company = TestData.getCompany();
        user.setRole(Role.BEGINNER);
    }

    @Test
    public void shouldResetTrackingCompaniesTest() {
        user.getTrackingCompanies().add(company);
        assertFalse(trackingService.shouldResetTrackingCompanies(user));

        user.setRole(Role.GUEST);
        assertTrue(trackingService.shouldResetTrackingCompanies(user));
    }

    @Test
    public void shouldTrack() throws NoSuchEntityException, ApiException {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        user.getTrackingCompanies().add(company);

        trackingService.track(user, company.getDisplaySymbol());
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void shouldFailIfAlreadyTracking() {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        user.setRole(Role.GUEST);
        assertThrows(ApiException.class, () -> trackingService.track(user, company.getDisplaySymbol()));
    }

    @Test
    public void shouldFailTrackIfNoSuchCompany() {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> trackingService.track(user, company.getDisplaySymbol()));
    }

    @Test
    public void shouldUnTrack() throws NoSuchEntityException {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        user.getTrackingCompanies().add(company);
        User userShouldBeSaved = User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole())
                .trackingCompanies(user.getTrackingCompanies())
                .build();
        userShouldBeSaved.getTrackingCompanies().remove(company);

        trackingService.unTrack(user, company.getDisplaySymbol());
        Mockito.verify(userRepository, Mockito.times(1)).save(userShouldBeSaved);
    }

    @Test
    public void shouldFailUnTrackIfNoSuchCompany() {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> trackingService.unTrack(user, company.getDisplaySymbol()));
    }
}
