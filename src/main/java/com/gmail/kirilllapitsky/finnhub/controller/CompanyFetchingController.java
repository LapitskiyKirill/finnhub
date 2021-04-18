package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.service.fetching.FetchingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        fetchingService.refreshAllCompaniesInfo();
    }

    @PostMapping("/allCompaniesMetrics")
    public void fetchAllCompaniesMetrics() {
        fetchingService.fetchAllCompaniesMetrics();
    }
}
