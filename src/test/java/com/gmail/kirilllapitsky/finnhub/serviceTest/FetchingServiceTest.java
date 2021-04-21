package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompany;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompanyInfo;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubStockData;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;
import com.gmail.kirilllapitsky.finnhub.client.CompanyFeignClient;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyMetricsRepository;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.DailyStockDataRepository;
import com.gmail.kirilllapitsky.finnhub.repository.StockDataRepository;
import com.gmail.kirilllapitsky.finnhub.service.fetching.FetchingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FetchingServiceTest {
    @InjectMocks
    private FetchingServiceImpl fetchingService;

    @Spy
    private CompanyFeignClient companyFeignClient;

    @Spy
    private CompanyRepository companyRepository;

    @Spy
    private CompanyMetricsRepository companyMetricsRepository;

    @Spy
    private StockDataRepository stockDataRepository;

    @Spy
    private DailyStockDataRepository dailyStockDataRepository;

    private List<Company> companiesShouldBeReturnedForSaving;
    private List<Company> companies;
    private Page<Company> resultPage;
    private Page<Company> emptyResultPage;

    @Before
    public void initialize() {
        companiesShouldBeReturnedForSaving = new ArrayList<>();
        companies = new ArrayList<>();
        companies.add(TestData.getCompany());
        resultPage = new PageImpl<>(companies);
        emptyResultPage = new PageImpl<>(Collections.EMPTY_LIST);
    }

    @Test
    public void shouldGetAllCompanies() {
        FinnhubCompany finnhubCompany = TestData.getFinnhubCompany();
        List<FinnhubCompany> parsedCompanies = new ArrayList<>();
        parsedCompanies.add(finnhubCompany);
        companiesShouldBeReturnedForSaving.add(Company
                .builder()
                .description(finnhubCompany.getDescription())
                .displaySymbol(finnhubCompany.getDisplaySymbol())
                .currency(finnhubCompany.getCurrency())
                .build());

        Mockito.when(companyFeignClient.fetchAllCompanies()).thenReturn(parsedCompanies);

        fetchingService.fetchAllCompanies();
        Mockito.verify(companyRepository, Mockito.times(1)).saveAll(companiesShouldBeReturnedForSaving);
    }

    @Test
    public void shouldRefreshAllCompaniesInfo() {
        FinnhubCompanyInfo finnhubCompanyInfo = TestData.getFinnhubCompanyInfoByCompany(TestData.getCompany());

        Mockito.when(companyRepository.findAll(PageRequest.of(0, 100))).thenReturn(resultPage);
        Mockito.when(companyRepository.findAll(PageRequest.of(1, 100))).thenReturn(emptyResultPage);
        Mockito.when(companyFeignClient.fetchCompanyInfo(companies.get(0).getDisplaySymbol())).thenReturn(finnhubCompanyInfo);
        companiesShouldBeReturnedForSaving.add(TestData.getCompany());

        fetchingService.refreshAllCompaniesInfo();
        Mockito.verify(companyRepository, Mockito.times(1)).saveAll(companiesShouldBeReturnedForSaving);
    }

    @Test
    public void shouldRefreshAllCompaniesMetrics() {
        CompanyMetrics companyMetrics = TestData.getCompanyMetrics();
        FinnhubCompanyMetrics finnhubCompanyMetrics;
        companyMetrics.setCompany(TestData.getCompany());
        finnhubCompanyMetrics = TestData.getFinnhubCompanyMetrics(companyMetrics);
        List<CompanyMetrics> companyMetricsShouldBeReturnedForSaving = new ArrayList<>();
        companyMetricsShouldBeReturnedForSaving.add(companyMetrics);

        Mockito.when(companyRepository.findAll(PageRequest.of(0, 100))).thenReturn(resultPage);
        Mockito.when(companyRepository.findAll(PageRequest.of(1, 100))).thenReturn(emptyResultPage);
        Mockito.when(companyFeignClient.fetchCompanyMetrics(companies.get(0).getDisplaySymbol())).thenReturn(finnhubCompanyMetrics);

        fetchingService.fetchAllCompaniesMetrics();
        Mockito.verify(companyMetricsRepository, Mockito.times(1)).saveAll(companyMetricsShouldBeReturnedForSaving);
    }

    @Test
    public void shouldFetchCompanyMetrics() {
        CompanyMetrics companyMetrics = TestData.getCompanyMetrics();
        FinnhubCompanyMetrics finnhubCompanyMetrics;
        companyMetrics.setCompany(TestData.getCompany());
        finnhubCompanyMetrics = TestData.getFinnhubCompanyMetrics(companyMetrics);
        List<CompanyMetrics> companyMetricsList = new ArrayList<>();
        List<CompanyMetrics> companyMetricsShouldBeReturnedForSaving = new ArrayList<>();
        companyMetricsList.add(companyMetrics);
        companyMetricsShouldBeReturnedForSaving.add(companyMetrics);
        Page<CompanyMetrics> resultPage = new PageImpl<>(companyMetricsList);
        Page<CompanyMetrics> emptyResultPage = new PageImpl<>(Collections.EMPTY_LIST);

        Mockito.when(companyMetricsRepository.findAll(PageRequest.of(0, 100))).thenReturn(resultPage);
        Mockito.when(companyMetricsRepository.findAll(PageRequest.of(1, 100))).thenReturn(emptyResultPage);
        Mockito.when(companyFeignClient.fetchCompanyMetrics(companyMetricsList.get(0).getCompany().getDisplaySymbol())).thenReturn(finnhubCompanyMetrics);

        fetchingService.refreshCompanyMetrics();
        Mockito.verify(companyMetricsRepository, Mockito.times(1)).saveAll(companyMetricsShouldBeReturnedForSaving);
    }

    @Test
    public void shouldFetchStockData() {
        StockData stockData = TestData.getStockData();
        stockData.setCompany(TestData.getCompany());
        FinnhubStockData finnhubStockData = TestData.getFinnhubStockData(stockData);
        List<StockData> stockDataShouldBeReturnedForSaving = new ArrayList<>();
        stockDataShouldBeReturnedForSaving.add(stockData);

        Mockito.when(companyRepository.findAll(PageRequest.of(0, 100))).thenReturn(resultPage);
        Mockito.when(companyRepository.findAll(PageRequest.of(1, 100))).thenReturn(emptyResultPage);
        Mockito.when(companyFeignClient.fetchCompanyStockData(companies.get(0).getDisplaySymbol())).thenReturn(finnhubStockData);

        fetchingService.fetchStockData();
        Mockito.verify(stockDataRepository, Mockito.times(1)).saveAll(stockDataShouldBeReturnedForSaving);
    }

    @Test
    public void shouldFetchDailyStockData() {
        DailyStockData dailyStockData = TestData.getDailyStockData();
        dailyStockData.setCompany(TestData.getCompany());
        FinnhubStockData finnhubStockData = TestData.getFinnhubStockData(dailyStockData);
        List<DailyStockData> stockDataShouldBeReturnedForSaving = new ArrayList<>();
        stockDataShouldBeReturnedForSaving.add(dailyStockData);

        Mockito.when(companyRepository.findAll(PageRequest.of(0, 100))).thenReturn(resultPage);
        Mockito.when(companyRepository.findAll(PageRequest.of(1, 100))).thenReturn(emptyResultPage);
        Mockito.when(companyFeignClient.fetchCompanyStockData(companies.get(0).getDisplaySymbol())).thenReturn(finnhubStockData);

        fetchingService.fetchDailyStockData();
        Mockito.verify(dailyStockDataRepository, Mockito.times(1)).saveAll(stockDataShouldBeReturnedForSaving);
    }
}
