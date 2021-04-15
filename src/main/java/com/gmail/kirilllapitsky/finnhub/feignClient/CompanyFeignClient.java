package com.gmail.kirilllapitsky.finnhub.feignClient;

import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompany;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompanyInfo;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedCompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.dto.ParsedStockData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CompanyFeignClient", url = "https://finnhub.io")
public interface CompanyFeignClient {

    @GetMapping(value = "api/v1/stock/symbol?exchange=US&token=${finnhub.token}")
    List<ParsedCompany> fetchAllCompanies();

    @GetMapping(value = "api/v1/stock/profile2?symbol={displaySymbol}&token=${finnhub.token}")
    ParsedCompanyInfo fetchCompanyInfo(@PathVariable("displaySymbol") String displaySymbol);

    @GetMapping(value = "api/v1/stock/metric?symbol={displaySymbol}&metric=all&token=${finnhub.token}")
    ParsedCompanyMetrics fetchCompanyMetrics(@PathVariable("displaySymbol") String displaySymbol);

    @GetMapping(value = "api/v1/quote?symbol={displaySymbol}&token=${finnhub.token}")
    ParsedStockData fetchCompanyStockData(@PathVariable("displaySymbol") String displaySymbol);
}
