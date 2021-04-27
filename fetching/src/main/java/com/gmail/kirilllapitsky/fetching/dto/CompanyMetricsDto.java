package com.gmail.kirilllapitsky.fetching.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CompanyMetricsDto {
    private String companyName;
    private Double yearDailyReturn;
    private Double halfYearDailyReturn;
    private Double quarterYearDailyReturn;
    private Double tenDayAverageTradingVolume;
    private Double yearHigh;
    private LocalDate yearHighDate;
    private Double yearLow;
    private LocalDate yearLowDate;
}
