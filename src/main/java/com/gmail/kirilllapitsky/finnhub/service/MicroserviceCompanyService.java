package com.gmail.kirilllapitsky.finnhub.service;

import com.gmail.kirilllapitsky.finnhub.client.TrackingClient;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyDto;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.finnhub.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.finnhub.dto.StockDataDto;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.User;
import com.gmail.kirilllapitsky.finnhub.exception.ApiException;
import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;
import com.gmail.kirilllapitsky.finnhub.repository.CompanyRepository;
import com.gmail.kirilllapitsky.finnhub.utils.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroserviceCompanyService {
    private final TrackingClient trackingClient;
    private final CompanyRepository companyRepository;

    public void fetchAllCompaniesFromFetchingService() {
        List<Company> companies = trackingClient.getAllCompanies();
        System.out.println(companies);
        companyRepository.saveAll(companies);
    }

    public List<CompanyDto> getAllCompanies(Pageable pageable) {
        return companyRepository.findAll(pageable).getContent().stream()
                .map(ServiceMapper::companyMapper)
                .collect(Collectors.toList());
    }

    public CompanyMetricsDto getCompanyMetrics(User user, String displaySymbol) throws NoSuchEntityException, ApiException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        checkForTracking(user, company);
        return trackingClient.getCompanyMetrics(displaySymbol);
    }

    public List<StockDataDto> getCompanyStockData(User user, String displaySymbol, int pageNumber, int pageSize) throws NoSuchEntityException, ApiException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        checkForTracking(user, company);
        return trackingClient.getCompanyStockData(displaySymbol, pageNumber, pageSize);
    }

    public List<DailyStockDataDto> getCompanyDailyStockData(User user, String displaySymbol, int pageNumber, int pageSize) throws NoSuchEntityException, ApiException {
        Company company = getCompanyByDisplaySymbol(displaySymbol);
        checkForTracking(user, company);
        return trackingClient.getCompanyDailyStockData(displaySymbol, pageNumber, pageSize);
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