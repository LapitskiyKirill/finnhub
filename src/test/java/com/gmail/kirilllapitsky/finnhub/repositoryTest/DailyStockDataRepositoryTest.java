package com.gmail.kirilllapitsky.finnhub.repositoryTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import com.gmail.kirilllapitsky.finnhub.repository.DailyStockDataRepository;
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
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DailyStockDataRepositoryTest {
    @Autowired
    private DailyStockDataRepository dailyStockDataRepository;

    private DailyStockData dailyStockData;
    private DailyStockData retrievedDailyStockData;

    @Before
    public void before() {
        dailyStockData = TestData.getDailyStockData();
    }

    @Test
    public void shouldSaveAndRetrieve() {
        dailyStockDataRepository.save(dailyStockData);
        retrievedDailyStockData = dailyStockDataRepository.findById(1L).get();
        dailyStockData.setId(retrievedDailyStockData.getId());

        assertEquals(retrievedDailyStockData, dailyStockData);
    }
}
