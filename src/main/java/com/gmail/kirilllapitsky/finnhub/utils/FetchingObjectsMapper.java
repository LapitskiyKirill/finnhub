package com.gmail.kirilllapitsky.finnhub.utils;

import com.gmail.kirilllapitsky.finnhub.dto.*;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;

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
        return Company.
                builder()
                .id(company.getId())
                .currency(company.getCurrency())
                .description(company.getDescription())
                .displaySymbol(company.getDisplaySymbol())
                .country(parsedCompanyInfo.getCountry())
                .exchange(parsedCompanyInfo.getExchange())
                .finnhubIndustry(parsedCompanyInfo.getExchange())
                .ipo(parsedCompanyInfo.getIpo())
                .logo(parsedCompanyInfo.getLogo())
                .name(parsedCompanyInfo.getName())
                .ticker(parsedCompanyInfo.getTicker())
                .webUrl(parsedCompanyInfo.getWeburl())
                .build();
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

    public static CompanyMetrics companyRenewableMetricsMapper(CompanyMetrics companyMetrics, ParsedCompanyMetrics parsedCompanyMetrics) {
        ParsedMetrics parsedMetrics = parsedCompanyMetrics.getParsedMetrics();
        return CompanyMetrics
                .builder()
                .id(companyMetrics.getId())
                .company(companyMetrics.getCompany())
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

    public static StockData stockDataMapper(Company company, ParsedStockData parsedStockData) {
        return StockData
                .builder()
                .company(company)
                .currentPrice(parsedStockData.getCurrentPrice())
                .highPrice(parsedStockData.getHighPrice())
                .lowPrice(parsedStockData.getLowPrice())
                .openPrice(parsedStockData.getOpenPrice())
                .build();
    }

    public static StockData renewableStockDataMapper(StockData stockData, ParsedStockData parsedStockData) {
        return StockData
                .builder()
                .id(stockData.getId())
                .company(stockData.getCompany())
                .currentPrice(parsedStockData.getCurrentPrice())
                .highPrice(parsedStockData.getHighPrice())
                .lowPrice(parsedStockData.getLowPrice())
                .openPrice(parsedStockData.getOpenPrice())
                .dailyMaxPercentageChange(stockData.getDailyMaxPercentageChange())
                .dailyMinPercentageChange(stockData.getDailyMinPercentageChange())
                .build();
    }

    public static StockData dailyRenewableStockDataMapper(StockData stockData, ParsedStockData parsedStockData) {
        return StockData
                .builder()
                .id(stockData.getId())
                .company(stockData.getCompany())
                .currentPrice(parsedStockData.getCurrentPrice())
                .highPrice(parsedStockData.getHighPrice())
                .lowPrice(parsedStockData.getLowPrice())
                .openPrice(parsedStockData.getOpenPrice())
                .dailyMaxPercentageChange((parsedStockData.getHighPrice() - stockData.getHighPrice()) / stockData.getHighPrice())
                .dailyMinPercentageChange((parsedStockData.getLowPrice() - stockData.getLowPrice()) / stockData.getLowPrice())
                .build();
    }
}
