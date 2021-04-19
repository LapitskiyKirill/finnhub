package com.gmail.kirilllapitsky.finnhub.repositoryTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;
import com.gmail.kirilllapitsky.finnhub.repository.StockDataRepository;
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
public class StockDataRepositoryTest {
    @Autowired
    private StockDataRepository stockDataRepository;

    private StockData stockData;
    private StockData retrievedStockData;

    @Before
    public void before() {
        stockData = TestData.getStockData();
    }

    @Test
    public void shouldSaveAndRetrieve() {
        stockDataRepository.save(stockData);
        retrievedStockData = stockDataRepository.findById(1L).get();
        stockData.setId(retrievedStockData.getId());

        assertEquals(retrievedStockData, stockData);
    }
}
