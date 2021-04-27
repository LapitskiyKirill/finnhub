package com.gmail.kirilllapitsky.fetching.utils;

import com.gmail.kirilllapitsky.fetching.dto.*;
import com.gmail.kirilllapitsky.fetching.entity.Company;
import com.gmail.kirilllapitsky.fetching.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.fetching.entity.DailyStockData;
import com.gmail.kirilllapitsky.fetching.entity.StockData;

public class FetchingObjectsMapper {

    public static Company companyMapper(FinnhubCompany finnhubCompany) {
        return Company.builder()
                .currency(finnhubCompany.getCurrency())
                .description(finnhubCompany.getDescription())
                .displaySymbol(finnhubCompany.getDisplaySymbol())
                .build();
    }

    public static Company companyInfoMapper(Company company, FinnhubCompanyInfo finnhubCompanyInfo) {
        company.setCountry(finnhubCompanyInfo.getCountry());
        company.setExchange(finnhubCompanyInfo.getExchange());
        company.setFinnhubIndustry(finnhubCompanyInfo.getFinnhubIndustry());
        company.setIpo(finnhubCompanyInfo.getIpo());
        company.setLogo(finnhubCompanyInfo.getLogo());
        company.setName(finnhubCompanyInfo.getName());
        company.setTicker(finnhubCompanyInfo.getTicker());
        company.setWebUrl(finnhubCompanyInfo.getWeburl());
        return company;
    }

    public static CompanyMetrics companyMetricsMapper(Company company, FinnhubCompanyMetrics finnhubCompanyMetrics) {
        FinnhubMetrics finnhubMetrics = finnhubCompanyMetrics.getFinnhubMetrics();
        return CompanyMetrics.builder()
                .company(company)
                .yearDailyReturn(finnhubMetrics.getYearDailyReturn())
                .halfYearDailyReturn(finnhubMetrics.getHalfYearDailyReturn())
                .quarterYearDailyReturn(finnhubMetrics.getQuarterYearDailyReturn())
                .tenDayAverageTradingVolume(finnhubMetrics.getTenDayAverageTradingVolume())
                .yearHigh(finnhubMetrics.getYearHigh())
                .yearHighDate(finnhubMetrics.getYearHighDate())
                .yearLow(finnhubMetrics.getYearLow())
                .yearLowDate(finnhubMetrics.getYearLowDate())
                .build();
    }

    public static CompanyMetrics renewableCompanyMetricsMapper(CompanyMetrics companyMetrics, FinnhubCompanyMetrics finnhubCompanyMetrics) {
        FinnhubMetrics finnhubMetrics = finnhubCompanyMetrics.getFinnhubMetrics();
        companyMetrics.setYearDailyReturn(finnhubMetrics.getYearDailyReturn());
        companyMetrics.setHalfYearDailyReturn(finnhubMetrics.getHalfYearDailyReturn());
        companyMetrics.setQuarterYearDailyReturn(finnhubMetrics.getQuarterYearDailyReturn());
        companyMetrics.setTenDayAverageTradingVolume(finnhubMetrics.getTenDayAverageTradingVolume());
        companyMetrics.setYearHigh(finnhubMetrics.getYearHigh());
        companyMetrics.setYearHighDate(finnhubMetrics.getYearHighDate());
        companyMetrics.setYearLow(finnhubMetrics.getYearLow());
        companyMetrics.setYearLowDate(finnhubMetrics.getYearLowDate());
        return companyMetrics;

    }

    public static StockData stockDataMapper(Company company, FinnhubStockData finnhubStockData) {
        return StockData.builder()
                .company(company)
                .currentPrice(finnhubStockData.getCurrentPrice())
                .openPrice(finnhubStockData.getOpenPrice())
                .build();
    }

    public static DailyStockData dailyStockDataMapper(Company company, FinnhubStockData finnhubStockData) {
        return DailyStockData.builder()
                .company(company)
                .highPrice(finnhubStockData.getHighPrice())
                .lowPrice(finnhubStockData.getLowPrice())
                .build();
    }
}
