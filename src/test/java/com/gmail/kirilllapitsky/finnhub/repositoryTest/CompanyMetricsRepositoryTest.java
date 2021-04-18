package com.gmail.kirilllapitsky.finnhub.repositoryTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyMetricsRepository;
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
public class CompanyMetricsRepositoryTest {
    @Autowired
    private CompanyMetricsRepository companyMetricsRepository;

    private CompanyMetrics companyMetrics;
    private CompanyMetrics retrievedCompanyMetrics;

    @Before
    public void before() {
        companyMetrics = TestData.getCompanyMetrics();
    }

    @Test
    public void shouldSaveAndRetrieve() {
        companyMetricsRepository.save(companyMetrics);
        retrievedCompanyMetrics = companyMetricsRepository.findById(1L).get();
        companyMetrics.setId(retrievedCompanyMetrics.getId());

        assertEquals(retrievedCompanyMetrics, companyMetrics);
    }
}
