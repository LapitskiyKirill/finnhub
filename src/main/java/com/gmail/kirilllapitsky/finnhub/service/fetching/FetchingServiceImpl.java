package com.gmail.kirilllapitsky.finnhub.service.fetching;

import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompany;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompanyInfo;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedStockData;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.feignClient.CompanyFeignClient;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyMetricsRepository;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.DailyStockDataRepository;
import com.gmail.kirilllapitsky.finnhub.repository.StockDataRepository;
import com.gmail.kirilllapitsky.finnhub.utils.FetchingObjectsMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.kirilllapitsky.finnhub.utils.FetchingObjectsMapper.*;

@Service
@AllArgsConstructor
public class FetchingServiceImpl implements FetchingService {
    private final CompanyFeignClient companyFeignClient;
    private final CompanyRepository companyRepository;
    private final CompanyMetricsRepository companyMetricsRepository;
    private final StockDataRepository stockDataRepository;
    private final DailyStockDataRepository dailyStockDataRepository;

    @Override
    public void fetchAllCompanies() {
        List<ParsedCompany> parsedCompanies = companyFeignClient.fetchAllCompanies();

        List<Company> companies = parsedCompanies
                .stream()
                .map(FetchingObjectsMapper::companyMapper)
                .limit(100)
                .collect(Collectors.toList());
        companyRepository.saveAll(companies);
    }

    @Override
    public void fetchAllCompaniesInfo() {
        List<Company> companies = companyRepository.findAll();

        List<Company> updatedCompanies = companies
                .stream()
                .map(company -> companyInfoMapper(
                        company,
                        companyFeignClient.fetchCompanyInfo(company.getDisplaySymbol()))
                )
                .collect(Collectors.toList());
        companyRepository.saveAll(updatedCompanies);

    }

    @Override
    public void fetchAllCompaniesMetrics() {
        List<Company> companies = companyRepository.findAll();

        List<CompanyMetrics> companyMetrics = companies
                .stream()
                .map(company -> companyMetricsMapper(company, companyFeignClient.fetchCompanyMetrics(company.getDisplaySymbol())))
                .collect(Collectors.toList());
        companyMetricsRepository.saveAll(companyMetrics);
    }

    @Override
    public void fetchAllCompaniesStockData() {
        List<Company> companies = companyRepository.findAll();

        List<StockData> stockDataList = companies
                .stream()
                .map(company -> stockDataMapper(company, companyFeignClient.fetchCompanyStockData(company.getDisplaySymbol())))
                .collect(Collectors.toList());
        stockDataRepository.saveAll(stockDataList);
    }

    @Override
    public void fetchCompanyMetrics(String displaySymbol) throws NoSuchEntityException {
        Company company = companyRepository.findByDisplaySymbol(displaySymbol)
                .orElseThrow(() -> new NoSuchEntityException("No company with such display symbol found."));

        ParsedCompanyMetrics parsedCompanyMetrics = companyFeignClient.fetchCompanyMetrics(displaySymbol);

        CompanyMetrics companyMetrics = companyMetricsMapper(company, parsedCompanyMetrics);
        companyMetricsRepository.save(companyMetrics);
    }

    @Override
    public void fetchCompanyStockData(String displaySymbol) throws NoSuchEntityException {
        Company company = companyRepository.findByDisplaySymbol(displaySymbol)
                .orElseThrow(() -> new NoSuchEntityException("No company with such display symbol found."));

        ParsedStockData parsedStockData = companyFeignClient.fetchCompanyStockData(displaySymbol);

        StockData stockData = stockDataMapper(company, parsedStockData);
        stockDataRepository.save(stockData);
    }

    @Override
    public void fetchCompanyInfo(String displaySymbol) throws NoSuchEntityException {
        Company company = companyRepository.findByDisplaySymbol(displaySymbol)
                .orElseThrow(() -> new NoSuchEntityException("No company with such display symbol found."));

        ParsedCompanyInfo parsedCompanyInfo = companyFeignClient.fetchCompanyInfo(displaySymbol);

        companyInfoMapper(company, parsedCompanyInfo);
        companyRepository.save(company);
    }

    @Scheduled(cron = "0 0 0 * * 0", zone = "Europe/Moscow")
    public void refreshCompanyMetrics() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<CompanyMetrics> resultPage = companyMetricsRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<CompanyMetrics> companiesMetrics = resultPage.getContent()
                        .stream()
                        .map(companyMetrics -> renewableCompanyMetricsMapper(
                                companyMetrics,
                                companyFeignClient.fetchCompanyMetrics(companyMetrics.getCompany().getDisplaySymbol())
                                )
                        )
                        .collect(Collectors.toList());
                companyMetricsRepository.saveAll(companiesMetrics);
                page++;
            } else {
                return;
            }
        }
    }


    @Scheduled(cron = "0 */30 * * * *", zone = "Europe/Moscow")
    public void refreshStockData() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<Company> resultPage = companyRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<StockData> companies = resultPage.getContent()
                        .stream()
                        .map(company -> stockDataMapper(company,
                                companyFeignClient.fetchCompanyStockData(company.getDisplaySymbol())
                                )
                        )
                        .collect(Collectors.toList());
                stockDataRepository.saveAll(companies);
                page++;
            } else {
                return;
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void dailyRefreshStockData() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<Company> resultPage = companyRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<DailyStockData> companies = resultPage.getContent()
                        .stream()
                        .map(company -> dailyStockDataMapper(company,
                                companyFeignClient.fetchCompanyStockData(company.getDisplaySymbol())
                                )
                        )
                        .collect(Collectors.toList());
                dailyStockDataRepository.saveAll(companies);
                page++;
            } else {
                return;
            }
        }
    }

}
