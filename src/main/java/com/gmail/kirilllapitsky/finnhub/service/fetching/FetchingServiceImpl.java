package com.gmail.kirilllapitsky.finnhub.service.fetching;

import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompany;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;
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
        List<FinnhubCompany> parsedCompanies = companyFeignClient.fetchAllCompanies();
        List<Company> companies = parsedCompanies
                .stream()
                .map(FetchingObjectsMapper::companyMapper)
                .limit(100)
                .collect(Collectors.toList());
        companyRepository.saveAll(companies);
    }

    @Override
    public void refreshAllCompaniesInfo() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<Company> resultPage = companyRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<Company> updatedCompanies = resultPage.getContent()
                        .stream()
                        .map(company -> companyInfoMapper(
                                company,
                                companyFeignClient.fetchCompanyInfo(company.getDisplaySymbol()))
                        )
                        .collect(Collectors.toList());
                companyRepository.saveAll(updatedCompanies);
                page++;
            } else {
                return;
            }
        }
    }

    @Override
    public void fetchAllCompaniesMetrics() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<Company> resultPage = companyRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<CompanyMetrics> companyMetricsList = resultPage.getContent()
                        .stream()
                        .map(company -> companyMetricsMapper(company, companyFeignClient.fetchCompanyMetrics(company.getDisplaySymbol())))
                        .collect(Collectors.toList());
                companyMetricsRepository.saveAll(companyMetricsList);
                page++;
            } else {
                return;
            }
        }
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
                List<CompanyMetrics> companyMetricsList = resultPage.getContent()
                        .stream()
                        .map(companyMetrics -> renewableCompanyMetricsMapper(
                                companyMetrics,
                                companyFeignClient.fetchCompanyMetrics(companyMetrics.getCompany().getDisplaySymbol())
                                )
                        )
                        .collect(Collectors.toList());
                companyMetricsRepository.saveAll(companyMetricsList);
                page++;
            } else {
                return;
            }
        }
    }

    @Scheduled(cron = "0 */10 * * * *", zone = "Europe/Moscow")
    public void fetchStockData() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<Company> resultPage = companyRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<StockData> stockDataList = resultPage.getContent()
                        .stream()
                        .map(company ->
                                stockDataMapper(company,
                                        companyFeignClient.fetchCompanyStockData(company.getDisplaySymbol())
                                )
                        )
                        .collect(Collectors.toList());
                stockDataRepository.saveAll(stockDataList);
                page++;
            } else {
                return;
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Moscow")
    public void fetchDailyStockData() {
        int page = 0;
        int pageSize = 100;
        Pageable pageable;
        while (true) {
            pageable = PageRequest.of(page, pageSize);
            Page<Company> resultPage = companyRepository.findAll(pageable);
            if (!resultPage.isEmpty()) {
                List<DailyStockData> dailyStockDataList = resultPage.getContent()
                        .stream()
                        .map(company -> dailyStockDataMapper(company,
                                companyFeignClient.fetchCompanyStockData(company.getDisplaySymbol())
                                )
                        )
                        .collect(Collectors.toList());
                dailyStockDataRepository.saveAll(dailyStockDataList);
                page++;
            } else {
                return;
            }
        }
    }
}
