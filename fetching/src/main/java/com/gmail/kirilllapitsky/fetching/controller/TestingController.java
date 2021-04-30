package com.gmail.kirilllapitsky.fetching.controller;

import com.gmail.kirilllapitsky.fetching.service.TestingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/testing")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestingController {
    private final TestingService testingService;

    @PostMapping("/fetchAllCompanies")
    public void fetchAllCompanies() {
        testingService.fetchAllCompanies();
    }

    @PostMapping("/refreshAllCompaniesInfo")
    public void refreshAllCompaniesInfo() {
        testingService.refreshAllCompaniesInfo();
    }

    @PostMapping("/fetchStockData")
    public void fetchStockData() {
        testingService.fetchStockData();
    }

    @PostMapping("/fetchDailyStockData")
    public void fetchDailyStockData() {
        testingService.fetchDailyStockData();
    }

    @PostMapping("/refreshCompanyMetrics")
    public void refreshCompanyMetrics() {
        testingService.refreshCompanyMetrics();
    }
}
