package com.gmail.kirilllapitsky.fetching.service;

import com.gmail.kirilllapitsky.fetching.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.fetching.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.fetching.dto.StockDataDto;
import com.gmail.kirilllapitsky.fetching.entity.Company;
import com.gmail.kirilllapitsky.fetching.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.fetching.repository.CompanyMetricsRepository;
import com.gmail.kirilllapitsky.fetching.repository.CompanyRepository;
import com.gmail.kirilllapitsky.fetching.repository.DailyStockDataRepository;
import com.gmail.kirilllapitsky.fetching.repository.StockDataRepository;
import com.gmail.kirilllapitsky.fetching.utils.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMetricsRepository companyMetricsRepository;
    private final DailyStockDataRepository dailyStockDataRepository;
    private final StockDataRepository stockDataRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public CompanyMetricsDto getCompanyMetrics(String displaySymbol) throws Exception {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        CompanyMetrics companyMetrics = companyMetricsRepository.findByCompany(company)
                .orElseThrow(() -> new Exception(String.format("No company metrics for %s.", company.getName())));
        return ServiceMapper.companyMetricsMapper(companyMetrics);
    }

    public List<StockDataDto> getCompanyStockData(String displaySymbol, Pageable pageable) throws Exception {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        return stockDataRepository.findAllByCompany(company, pageable).stream()
                .map(ServiceMapper::stockDataMapper)
                .collect(Collectors.toList());
    }

    public List<DailyStockDataDto> getCompanyDailyStockData(String displaySymbol, Pageable pageable) throws Exception {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        return dailyStockDataRepository.findAllByCompany(company, pageable).stream()
                .map(ServiceMapper::dailyStockDataMapper)
                .collect(Collectors.toList());
    }

    private Company getCompanyByDisplaySymbol(String displaySymbol) throws Exception {
        return companyRepository.findByDisplaySymbol(displaySymbol)
                .orElseThrow(() -> new Exception(String.format("No company with display symbol %s.", displaySymbol)));
    }
}
