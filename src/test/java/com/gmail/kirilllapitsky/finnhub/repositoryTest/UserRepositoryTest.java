package com.gmail.kirilllapitsky.finnhub.repositoryTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;
    private User retrievedUser;

    @Before
    public void before() {
        user = TestData.getUser();
        userRepository.save(user);
    }

    @Test
    public void shouldSaveAndRetrieveByUsername() {
        retrievedUser = userRepository.findByUsername(user.getUsername()).get();

        assertEquals(retrievedUser.getUsername(), user.getUsername());
    }

    @Test
    public void shouldSaveAndRetrieveByEmail() {
        retrievedUser = userRepository.findByEmail(user.getEmail()).get();

        assertEquals(retrievedUser.getEmail(), user.getEmail());
    }
}
