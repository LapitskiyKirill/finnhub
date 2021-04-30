package com.gmail.kirilllapitsky.finnhub.controller;

import com.gmail.kirilllapitsky.finnhub.client.FetchingTestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fetchingTest")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchingTestController {
    private final FetchingTestClient fetchingTestClient;

    @PostMapping("/fetchAllCompanies")
    public ResponseEntity<String> fetchAllCompanies() {
        fetchingTestClient.fetchAllCompanies();
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/refreshAllCompaniesInfo")
    public ResponseEntity<String> refreshAllCompaniesInfo() {
        fetchingTestClient.refreshAllCompaniesInfo();
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/fetchStockData")
    public ResponseEntity<String> fetchStockData() {
        fetchingTestClient.fetchStockData();
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/fetchDailyStockData")
    public ResponseEntity<String> fetchDailyStockData() {
        fetchingTestClient.fetchDailyStockData();
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/refreshCompanyMetrics")
    public ResponseEntity<String> refreshCompanyMetrics() {
        fetchingTestClient.refreshCompanyMetrics();
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

}
