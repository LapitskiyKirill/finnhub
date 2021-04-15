package com.gmail.kirilllapitsky.finnhub.service.fetching;

import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompany;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompanyInfo;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedStockData;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.feignClient.CompanyFeignClient;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyMetricsRepository;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.StockDataRepository;
import com.gmail.kirilllapitsky.finnhub.utils.FetchingObjectsMapper;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gmail.kirilllapitsky.finnhub.utils.FetchingObjectsMapper.*;

@Service
@AllArgsConstructor
public class CompanyFetchingService implements FetchingService {
    private final CompanyFeignClient companyFeignClient;
    private final CompanyRepository companyRepository;
    private final CompanyMetricsRepository companyMetricsRepository;
    private final StockDataRepository stockDataRepository;

    @Override
    public void fetchAllCompanies() {
        List<ParsedCompany> parsedCompanies = companyFeignClient.fetchAllCompanies();
        parsedCompanies
                .stream()
                .map(FetchingObjectsMapper::companyMapper)
                .limit(100)
                .forEach(companyRepository::save);
    }

    @Override
    public void fetchAllCompaniesInfo() {
        List<Company> companies = companyRepository.findAll();

        companies
                .stream()
                .map(company -> companyInfoMapper(company, companyFeignClient.fetchCompanyInfo(company.getDisplaySymbol())))
                .forEach(companyRepository::save);
    }

    @Override
    public void fetchAllCompaniesMetrics() {
        List<Company> companies = companyRepository.findAll();

        companies
                .stream()
                .map(company -> companyMetricsMapper(company, companyFeignClient.fetchCompanyMetrics(company.getDisplaySymbol())))
                .forEach(companyMetricsRepository::save);
    }

    @Override
    public void fetchAllCompaniesStockData() {
        List<Company> companies = companyRepository.findAll();

        companies
                .stream()
                .map(company -> stockDataMapper(company, companyFeignClient.fetchCompanyStockData(company.getDisplaySymbol())))
                .forEach(stockDataRepository::save);
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

        company = companyInfoMapper(company, parsedCompanyInfo);
        companyRepository.save(company);
    }

    @Scheduled(cron = "0 0 0 * * 0", zone = "Europe/Moscow")
    public void refreshCompanyMetrics() {
        List<CompanyMetrics> companyMetricsList = companyMetricsRepository.findAll();

        companyMetricsList
                .stream()
                .map(companyMetrics -> companyRenewableMetricsMapper(companyMetrics, companyFeignClient.fetchCompanyMetrics(companyMetrics.getCompany().getDisplaySymbol())))
                .forEach(companyMetricsRepository::save);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void refreshStockData() {
        List<StockData> stockDataList = stockDataRepository.findAll();

        stockDataList
                .stream()
                .map(stockData -> renewableStockDataMapper(stockData, companyFeignClient.fetchCompanyStockData(stockData.getCompany().getDisplaySymbol())))
                .forEach(stockDataRepository::save);
    }
}
