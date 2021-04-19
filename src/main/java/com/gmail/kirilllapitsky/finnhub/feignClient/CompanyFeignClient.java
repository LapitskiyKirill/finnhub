package com.gmail.kirilllapitsky.finnhub.feignClient;

import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompany;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompanyInfo;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubCompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.dto.FinnhubStockData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CompanyFeignClient", url = "https://finnhub.io")
public interface CompanyFeignClient {

    @GetMapping(value = "api/v1/stock/symbol?exchange=US&token=${finnhub.token}")
    List<FinnhubCompany> fetchAllCompanies();

    @GetMapping(value = "api/v1/stock/profile2?symbol={displaySymbol}&token=${finnhub.token}")
    FinnhubCompanyInfo fetchCompanyInfo(@PathVariable("displaySymbol") String displaySymbol);

    @GetMapping(value = "api/v1/stock/metric?symbol={displaySymbol}&metric=all&token=${finnhub.token}")
    FinnhubCompanyMetrics fetchCompanyMetrics(@PathVariable("displaySymbol") String displaySymbol);

    @GetMapping(value = "api/v1/quote?symbol={displaySymbol}&token=${finnhub.token}")
    FinnhubStockData fetchCompanyStockData(@PathVariable("displaySymbol") String displaySymbol);
}
