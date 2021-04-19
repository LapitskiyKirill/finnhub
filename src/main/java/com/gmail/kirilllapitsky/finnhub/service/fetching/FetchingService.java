package com.gmail.kirilllapitsky.finnhub.service.fetching;

public interface FetchingService {
    void fetchAllCompanies();

    void refreshAllCompaniesInfo();

    void fetchAllCompaniesMetrics();
}
