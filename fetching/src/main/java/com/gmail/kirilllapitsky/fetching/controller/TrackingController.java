package com.gmail.kirilllapitsky.fetching.controller;

import com.gmail.kirilllapitsky.fetching.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.fetching.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.fetching.dto.StockDataDto;
import com.gmail.kirilllapitsky.fetching.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrackingController {
    private final CompanyService companyService;

    @GetMapping("/getCompanyMetrics")
    public CompanyMetricsDto getCompanyMetrics(@RequestParam("displaySymbol") String displaySymbol) throws Exception {
        return companyService.getCompanyMetrics(displaySymbol);
    }

    @GetMapping("/getCompanyStockData")
    public List<StockDataDto> getCompanyStockData(@RequestParam("displaySymbol") String displaySymbol,
                                                  @RequestParam("pageNumber") int pageNumber,
                                                  @RequestParam("pageSize") int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return companyService.getCompanyStockData(displaySymbol, pageable);
    }

    @GetMapping("/getCompanyDailyStockData")
    public List<DailyStockDataDto> getCompanyDailyStockData(@RequestParam("displaySymbol") String displaySymbol,
                                                            @RequestParam("pageNumber") int pageNumber,
                                                            @RequestParam("pageSize") int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return companyService.getCompanyDailyStockData(displaySymbol, pageable);
    }

}