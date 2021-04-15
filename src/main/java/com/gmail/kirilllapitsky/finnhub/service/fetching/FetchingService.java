package com.gmail.kirilllapitsky.finnhub.service.fetching;

import com.gmail.kirilllapitsky.finnhub.exception.NoSuchEntityException;

public interface FetchingService {
    void fetchAllCompanies();

    void fetchAllCompaniesInfo();

    void fetchAllCompaniesMetrics();

    void fetchAllCompaniesStockData();

    void fetchCompanyInfo(String displaySymbol) throws NoSuchEntityException;

    void fetchCompanyMetrics(String displaySymbol) throws NoSuchEntityException;

    void fetchCompanyStockData(String displaySymbol) throws NoSuchEntityException;
}
