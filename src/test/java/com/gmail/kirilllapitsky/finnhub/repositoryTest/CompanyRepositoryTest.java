package com.gmail.kirilllapitsky.finnhub.repositoryTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
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
public class CompanyRepositoryTest {
    @Autowired
    private CompanyRepository companyRepository;

    private Company company;
    private Company retrievedCompany;

    @Before
    public void before() {
        company = TestData.getCompany();
    }

    @Test
    public void shouldSaveAndRetrieveByDisplaySymbol() {
        companyRepository.save(company);
        retrievedCompany = companyRepository.findByDisplaySymbol(company.getDisplaySymbol()).get();
        company.setId(retrievedCompany.getId());

        assertEquals(retrievedCompany, company);
    }
}
