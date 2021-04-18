package com.gmail.kirilllapitsky.finnhub.utils;

import com.gmail.kirilllapitsky.finnhub.dto.*;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;

import java.time.LocalDateTime;

public class FetchingObjectsMapper {

    public static Company companyMapper(ParsedCompany parsedCompany) {
        return Company.
                builder()
                .currency(parsedCompany.getCurrency())
                .description(parsedCompany.getDescription())
                .displaySymbol(parsedCompany.getDisplaySymbol())
                .build();
    }

    public static Company companyInfoMapper(Company company, ParsedCompanyInfo parsedCompanyInfo) {
        company.setCountry(parsedCompanyInfo.getCountry());
        company.setExchange(parsedCompanyInfo.getExchange());
        company.setFinnhubIndustry(parsedCompanyInfo.getFinnhubIndustry());
        company.setIpo(parsedCompanyInfo.getIpo());
        company.setLogo(parsedCompanyInfo.getLogo());
        company.setName(parsedCompanyInfo.getName());
        company.setTicker(parsedCompanyInfo.getTicker());
        company.setWebUrl(parsedCompanyInfo.getWeburl());
        return company;
    }

    public static CompanyMetrics companyMetricsMapper(Company company, ParsedCompanyMetrics parsedCompanyMetrics) {
        ParsedMetrics parsedMetrics = parsedCompanyMetrics.getParsedMetrics();
        return CompanyMetrics
                .builder()
                .company(company)
                .yearDailyReturn(parsedMetrics.getYearDailyReturn())
                .halfYearDailyReturn(parsedMetrics.getHalfYearDailyReturn())
                .quarterYearDailyReturn(parsedMetrics.getQuarterYearDailyReturn())
                .tenDayAverageTradingVolume(parsedMetrics.getTenDayAverageTradingVolume())
                .yearHigh(parsedMetrics.getYearHigh())
                .yearHighDate(parsedMetrics.getYearHighDate())
                .yearLow(parsedMetrics.getYearLow())
                .yearLowDate(parsedMetrics.getYearLowDate())
                .build();
    }

    public static CompanyMetrics renewableCompanyMetricsMapper(CompanyMetrics companyMetrics, ParsedCompanyMetrics parsedCompanyMetrics) {
        ParsedMetrics parsedMetrics = parsedCompanyMetrics.getParsedMetrics();
        companyMetrics.setYearDailyReturn(parsedMetrics.getYearDailyReturn());
        companyMetrics.setHalfYearDailyReturn(parsedMetrics.getHalfYearDailyReturn());
        companyMetrics.setQuarterYearDailyReturn(parsedMetrics.getQuarterYearDailyReturn());
        companyMetrics.setTenDayAverageTradingVolume(parsedMetrics.getTenDayAverageTradingVolume());
        companyMetrics.setYearHigh(parsedMetrics.getYearHigh());
        companyMetrics.setYearHighDate(parsedMetrics.getYearHighDate());
        companyMetrics.setYearLow(parsedMetrics.getYearLow());
        companyMetrics.setYearLowDate(parsedMetrics.getYearLowDate());
        return companyMetrics;

    }

    public static StockData stockDataMapper(Company company, ParsedStockData parsedStockData) {
        return StockData
                .builder()
                .company(company)
                .currentPrice(parsedStockData.getCurrentPrice())
                .openPrice(parsedStockData.getOpenPrice())
                .build();
    }

    public static DailyStockData dailyStockDataMapper(Company company, ParsedStockData parsedStockData) {
        return DailyStockData
                .builder()
                .company(company)
                .highPrice(parsedStockData.getHighPrice())
                .lowPrice(parsedStockData.getLowPrice())
                .build();
    }
}
