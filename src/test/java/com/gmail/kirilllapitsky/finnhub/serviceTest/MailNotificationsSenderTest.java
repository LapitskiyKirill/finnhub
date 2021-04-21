package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.service.MailNotificationsSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MailNotificationsSenderTest {
    @InjectMocks
    private MailNotificationsSender mailNotificationsSender;

    @Spy
    private JavaMailSender mailSender;

    private List<String> emails;
    @Value("$spring.mail.username")
    String message;

    @Before
    public void initialize() {
        emails = TestData.getEmails();
    }

    @Test
    public void shouldPassUsersWithEndingSubscriptionToNotify() {
        Mockito.doNothing().when(mailSender).send((MimeMessagePreparator) any());

        mailNotificationsSender.notifyUsers(emails, message);
        Mockito.verify(mailSender, Mockito.times(emails.size())).send((MimeMessagePreparator) any());
    }
}
