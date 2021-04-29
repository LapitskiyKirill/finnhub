package com.gmail.kirilllapitsky.fetching.utils;

import com.gmail.kirilllapitsky.fetching.dto.CompanyMetricsDto;
import com.gmail.kirilllapitsky.fetching.dto.DailyStockDataDto;
import com.gmail.kirilllapitsky.fetching.dto.StockDataDto;
import com.gmail.kirilllapitsky.fetching.entity.CompanyMetrics;
import com.gmail.kirilllapitsky.fetching.entity.DailyStockData;
import com.gmail.kirilllapitsky.fetching.entity.StockData;

public class ServiceMapper {
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