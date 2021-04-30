package com.gmail.kirilllapitsky.finnhub.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "FetchingTestClient", url = "${fetching.path}")
public interface FetchingTestClient {

    @PostMapping(value = "/api/testing/fetchAllCompanies")
    void fetchAllCompanies();

    @PostMapping(value = "/api/testing/refreshAllCompaniesInfo")
    void refreshAllCompaniesInfo();

    @PostMapping(value = "/api/testing/fetchStockData")
    void fetchStockData();

    @PostMapping(value = "/api/testing/fetchDailyStockData")
    void fetchDailyStockData();

    @PostMapping(value = "/api/testing/refreshCompanyMetrics")
    void refreshCompanyMetrics();
}