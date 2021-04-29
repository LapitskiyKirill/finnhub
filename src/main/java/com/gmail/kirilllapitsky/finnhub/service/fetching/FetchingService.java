package com.gmail.kirilllapitsky.finnhub.service.fetching;

@Deprecated
public interface FetchingService {
    void fetchAllCompanies();

    void refreshAllCompaniesInfo();

    void fetchAllCompaniesMetrics();
}
