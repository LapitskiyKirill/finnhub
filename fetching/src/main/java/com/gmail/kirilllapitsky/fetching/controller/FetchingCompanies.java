package com.gmail.kirilllapitsky.fetching.controller;

import com.gmail.kirilllapitsky.fetching.entity.Company;
import com.gmail.kirilllapitsky.fetching.service.CompanyService;
import com.gmail.kirilllapitsky.fetching.service.FetchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchingCompanies {
    private final CompanyService companyService;
    private final FetchingService fetchingService;

    @GetMapping("/getall")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PostMapping("/fetchAllCompanies")
    public void fetchAllCompanies() {
        fetchingService.fetchAllCompanies();
    }

    @PostMapping("/fetchAllCompaniesMetrics")
    public void fetchAllCompaniesMetrics() {
        fetchingService.fetchAllCompaniesMetrics();
    }

    @GetMapping("/refreshAllCompaniesInfo")
    public void refreshAllCompaniesInfo() {
        fetchingService.refreshAllCompaniesInfo();
    }
}
