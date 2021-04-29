package com.gmail.kirilllapitsky.finnhub.client;

import com.gmail.kirilllapitsky.finnhub.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.finnhub.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.finnhub.dto.StockDataDto;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "TrackingClient", url = "${fetching.path}")
public interface TrackingClient {
    @GetMapping(value = "/api/tracking/getCompanyMetrics")
    CompanyMetricsDto getCompanyMetrics(@RequestParam("displaySymbol") String displaySymbol);

    @GetMapping(value = "/api/tracking/getCompanyDailyStockData")
    List<DailyStockDataDto> getCompanyDailyStockData(@RequestParam("displaySymbol") String displaySymbol,
                                                     @RequestParam("pageNumber") int pageNumber,
                                                     @RequestParam("pageSize") int pageSize);

    @GetMapping(value = "/api/tracking/getCompanyStockData")
    List<StockDataDto> getCompanyStockData(@RequestParam("displaySymbol") String displaySymbol,
                                           @RequestParam("pageNumber") int pageNumber,
                                           @RequestParam("pageSize") int pageSize);

    @GetMapping(value = "/api/companies/getall")
    List<Company> getAllCompanies();
}
