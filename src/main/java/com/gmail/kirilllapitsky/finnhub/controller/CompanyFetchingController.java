package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.service.fetching.FetchingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetch")
@AllArgsConstructor
public class CompanyFetchingController {

    private final FetchingService fetchingService;

    @PostMapping("/allCompanies")
    public void fetchAllCompanies() {
        fetchingService.fetchAllCompanies();
    }

    @PostMapping("/allCompaniesInfo")
    public void fetchAllCompaniesInfo() {
        fetchingService.fetchAllCompaniesInfo();
    }

    @PostMapping("/allCompaniesMetrics")
    public void fetchAllCompaniesMetrics() {
        fetchingService.fetchAllCompaniesMetrics();
    }

    @PostMapping("/allCompaniesStockData")
    public void fetchAllCompaniesStockData() {
        fetchingService.fetchAllCompaniesStockData();
    }

    @PostMapping("/companyInfo")
    public void fetchCompanyInfo(@RequestParam(name = "displaySymbol") String displaySymbol) throws Exception {
        fetchingService.fetchCompanyInfo(displaySymbol);
    }

    @PostMapping("/companyMetrics")
    public void fetchCompanyMetrics(@RequestParam(name = "displaySymbol") String displaySymbol) throws Exception {
        fetchingService.fetchCompanyMetrics(displaySymbol);
    }

    @PostMapping("/stockData")
    public void fetchStockData(@RequestParam(name = "displaySymbol") String displaySymbol) throws Exception {
        fetchingService.fetchCompanyStockData(displaySymbol);
    }
}
