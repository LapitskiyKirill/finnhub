package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.dto.CompanyDto;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.finnhub.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.finnhub.dto.StockDataDto;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyMetricsRepository;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.repository.DailyStockDataRepository;
import com.gmail.kirilllapitsky.finnhub.repository.StockDataRepository;
import com.gmail.kirilllapitsky.finnhub.utils.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Deprecated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMetricsRepository companyMetricsRepository;
    private final DailyStockDataRepository dailyStockDataRepository;
    private final StockDataRepository stockDataRepository;

    public List<CompanyDto> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).getContent().stream()
                .map(ServiceMapper::companyMapper)
                .collect(Collectors.toList());
    }

    public CompanyMetricsDto getCompanyMetrics(User user, String displaySymbol) throws NoSuchEntityException, ApiException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        checkForTracking(user, company);
        CompanyMetrics companyMetrics = companyMetricsRepository.findByCompany(company)
                .orElseThrow(() -> new NoSuchEntityException(String.format("No company metrics for %s.", company.getName())));
        return ServiceMapper.companyMetricsMapper(companyMetrics);
    }

    public List<StockDataDto> getCompanyStockData(User user, String displaySymbol, Pageable pageable) throws NoSuchEntityException, ApiException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        checkForTracking(user, company);
        return stockDataRepository.findAllByCompany(company, pageable).stream()
                .map(ServiceMapper::stockDataMapper)
                .collect(Collectors.toList());
    }

    public List<DailyStockDataDto> getCompanyDailyStockData(User user, String displaySymbol, Pageable pageable) throws NoSuchEntityException, ApiException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        checkForTracking(user, company);
        return dailyStockDataRepository.findAllByCompany(company, pageable).stream()
                .map(ServiceMapper::dailyStockDataMapper)
                .collect(Collectors.toList());
    }

    public List<CompanyDto> getTrackedCompanies(User user) {
        return user.getTrackingCompanies().stream()
                .map(ServiceMapper::companyMapper)
                .collect(Collectors.toList());
    }

    private Company getCompanyByDisplaySymbol(String displaySymbol) throws NoSuchEntityException {
        return companyRepository.findByDisplaySymbol(displaySymbol)
                .orElseThrow(() -> new NoSuchEntityException(String.format("No company with display symbol %s.", displaySymbol)));
    }

    private void checkForTracking(User user, Company company) throws ApiException {
        if (!user.getTrackingCompanies().contains(company))
            throw new ApiException(String.format("You don`t tracking company %s.", company.getName()));
    }
}