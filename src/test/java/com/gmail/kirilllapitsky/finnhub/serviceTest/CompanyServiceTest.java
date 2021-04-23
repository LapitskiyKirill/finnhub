package com.gmail.kirilllapitsky.finnhub.serviceTest;

import com.gmail.kirilllapitsky.finnhub.TestData;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyDto;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.finnhub.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.finnhub.dto.StockDataDto;
import com.gmail.kirilllapitsky.finnhub.entity.*;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyMetricsRepository;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.DailyStockDataRepository;
import com.gmail.kirilllapitsky.finnhub.repository.StockDataRepository;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import com.gmail.kirilllapitsky.finnhub.service.CompanyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompanyServiceTest {
    @InjectMocks
    private CompanyService companyService;
    @Spy
    private CompanyRepository companyRepository;
    @Spy
    private CompanyMetricsRepository companyMetricsRepository;
    @Spy
    private DailyStockDataRepository dailyStockDataRepository;
    @Spy
    private StockDataRepository stockDataRepository;
    private Pageable pageable;
    private Company company;
    private CompanyDto companyDto;
    private User user;

    @Before
    public void initialize() {
        user = TestData.getUser();
        user.setTrackingCompanies(new HashSet<>());
        user.setRole(Role.BEGINNER);
        pageable = PageRequest.of(0, 5);
        company = TestData.getCompany();
        companyDto = CompanyDto.builder()
                .country(company.getCountry())
                .currency(company.getCurrency())
                .description(company.getDescription())
                .displaySymbol(company.getDisplaySymbol())
                .exchange(company.getExchange())
                .ipo(company.getIpo())
                .finnhubIndustry(company.getFinnhubIndustry())
                .logo(company.getLogo())
                .name(company.getName())
                .ticker(company.getTicker())
                .webUrl(company.getWebUrl())
                .build();
    }

    @Test
    public void shouldGetCompanies() {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        List<Company> companyList = new ArrayList<>();
        for (int i = 0; i < pageable.getPageSize(); i++) {
            companyDtoList.add(companyDto);
            companyList.add(company);
        }
        Mockito.when(companyRepository.findAll(pageable)).thenReturn(new PageImpl<>(companyList));

        List<CompanyDto> returnedList = companyService.getAllCompanies(pageable);

        assertEquals(returnedList, companyDtoList);
    }

    @Test
    public void shouldGetCompanyMetrics() throws ApiException, NoSuchEntityException {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        user.getTrackingCompanies().add(company);
        CompanyMetrics companyMetrics = TestData.getCompanyMetrics();
        companyMetrics.setCompany(company);
        Mockito.when(companyMetricsRepository.findByCompany(company)).thenReturn(Optional.of(companyMetrics));
        CompanyMetricsDto companyMetricsDto = CompanyMetricsDto.builder()
                .companyName(companyMetrics.getCompany().getName())
                .yearDailyReturn(companyMetrics.getYearDailyReturn())
                .halfYearDailyReturn(companyMetrics.getHalfYearDailyReturn())
                .quarterYearDailyReturn(companyMetrics.getQuarterYearDailyReturn())
                .tenDayAverageTradingVolume(companyMetrics.getTenDayAverageTradingVolume())
                .yearHigh(companyMetrics.getYearHigh())
                .yearHighDate(companyMetrics.getYearHighDate())
                .yearLow(companyMetrics.getYearLow())
                .yearLowDate(companyMetrics.getYearLowDate())
                .build();

        CompanyMetricsDto returnedCompanyMetricsDto = companyService.getCompanyMetrics(user, company.getDisplaySymbol());
        assertEquals(returnedCompanyMetricsDto, companyMetricsDto);
    }

    @Test
    public void shouldFailGetCompanyMetricsIfNoSuchCompany() {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.empty());
        assertThrows(NoSuchEntityException.class, () -> companyService.getCompanyMetrics(user, company.getDisplaySymbol()));
    }

    @Test
    public void shouldFailGetCompanyMetricsIfNotTracked() {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        assertThrows(ApiException.class, () -> companyService.getCompanyMetrics(user, company.getDisplaySymbol()));
    }

    @Test
    public void shouldGetCompanyStockData() throws ApiException, NoSuchEntityException {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        user.getTrackingCompanies().add(company);

        StockData stockData = TestData.getStockData();
        stockData.setCompany(company);
        StockDataDto stockDataDto = StockDataDto.builder()
                .companyName(stockData.getCompany().getName())
                .currentPrice(stockData.getCurrentPrice())
                .openPrice(stockData.getOpenPrice())
                .trackTime(stockData.getTrackTime())
                .build();

        List<StockDataDto> stockDataDtoList = new ArrayList<>();
        List<StockData> stockDataList = new ArrayList<>();
        for (int i = 0; i < pageable.getPageSize(); i++) {
            stockDataDtoList.add(stockDataDto);
            stockDataList.add(stockData);
        }

        Mockito.when(stockDataRepository.findAllByCompany(company, pageable)).thenReturn(stockDataList);


        List<StockDataDto> returnedStockDataDtoList = companyService.getCompanyStockData(user, company.getDisplaySymbol(), pageable);
        assertEquals(returnedStockDataDtoList, stockDataDtoList);
    }

    @Test
    public void shouldGetDailyCompanyStockData() throws ApiException, NoSuchEntityException {
        Mockito.when(companyRepository.findByDisplaySymbol(company.getDisplaySymbol())).thenReturn(Optional.of(company));
        user.getTrackingCompanies().add(company);

        DailyStockData dailyStockData = TestData.getDailyStockData();
        dailyStockData.setCompany(company);
        DailyStockDataDto dailyStockDataDto = DailyStockDataDto.builder()
                .companyName(dailyStockData.getCompany().getName())
                .highPrice(dailyStockData.getHighPrice())
                .lowPrice(dailyStockData.getLowPrice())
                .trackTime(dailyStockData.getTrackTime())
                .build();

        List<DailyStockDataDto> dailyStockDataDtoList = new ArrayList<>();
        List<DailyStockData> dailyStockDataList = new ArrayList<>();
        for (int i = 0; i < pageable.getPageSize(); i++) {
            dailyStockDataDtoList.add(dailyStockDataDto);
            dailyStockDataList.add(dailyStockData);
        }

        Mockito.when(dailyStockDataRepository.findAllByCompany(company, pageable)).thenReturn(dailyStockDataList);


        List<DailyStockDataDto> returnedDailyStockDataDtoList = companyService.getCompanyDailyStockData(user, company.getDisplaySymbol(), pageable);
        assertEquals(returnedDailyStockDataDtoList, dailyStockDataDtoList);
    }

    @Test
    public void shouldGetTrackedCompanies() {
        user.getTrackingCompanies().add(company);
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (int i = 0; i < user.getTrackingCompanies().size(); i++) {
            companyDtoList.add(companyDto);
        }

        List<CompanyDto> returnedList = companyService.getTrackedCompanies(user);

        assertEquals(returnedList, companyDtoList);
    }

}
