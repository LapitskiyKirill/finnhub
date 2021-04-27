package com.gmail.kirilllapitsky.finnhub.utils;

import com.gmail.kirilllapitsky.finnhub.dto.CompanyDto;
import com.gmail.kirilllapitsky.finnhub.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.finnhub.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.finnhub.dto.StockDataDto;
import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;

@Deprecated
public class ServiceMapper {
    public static CompanyDto companyMapper(Company company) {
        return CompanyDto.builder()
                .country(company.getCountry())
                .currency(company.getCurrency())
                .description(company.getDescription())
                .displaySymbol(company.getDisplaySymbol())
                .exchange(company.getExchange())
                .ipo(company.getIpo())
                .finnhubIndustry(company.getFinnhubIndustry())
                .logo(company.getLogo())
                .name(company.getName())
                .ticker(company.getTicker())
                .webUrl(company.getWebUrl())
                .build();
    }

    public static StockDataDto stockDataMapper(StockData stockData) {
        return StockDataDto.builder()
                .companyName(stockData.getCompany().getName())
                .currentPrice(stockData.getCurrentPrice())
                .openPrice(stockData.getOpenPrice())
                .trackTime(stockData.getTrackTime())
                .build();
    }

    public static DailyStockDataDto dailyStockDataMapper(DailyStockData dailyStockData) {
        return DailyStockDataDto.builder()
                .companyName(dailyStockData.getCompany().getName())
                .highPrice(dailyStockData.getHighPrice())
                .lowPrice(dailyStockData.getLowPrice())
                .trackTime(dailyStockData.getTrackTime())
                .build();

    }

    public static CompanyMetricsDto companyMetricsMapper(CompanyMetrics companyMetrics) {
        return CompanyMetricsDto.builder()
                .companyName(companyMetrics.getCompany().getName())
                .yearDailyReturn(companyMetrics.getYearDailyReturn())
                .halfYearDailyReturn(companyMetrics.getHalfYearDailyReturn())
                .quarterYearDailyReturn(companyMetrics.getQuarterYearDailyReturn())
                .tenDayAverageTradingVolume(companyMetrics.getTenDayAverageTradingVolume())
                .yearHigh(companyMetrics.getYearHigh())
                .yearHighDate(companyMetrics.getYearHighDate())
                .yearLow(companyMetrics.getYearLow())
                .yearLowDate(companyMetrics.getYearLowDate())
                .build();
    }
}
