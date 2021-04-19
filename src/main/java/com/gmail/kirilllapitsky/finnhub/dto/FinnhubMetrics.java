package com.gmail.kirilllapitsky.finnhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinnhubMetrics {
    @JsonProperty("52WeekPriceReturnDaily")
    private Double yearDailyReturn;
    @JsonProperty("26WeekPriceReturnDaily")
    private Double halfYearDailyReturn;
    @JsonProperty("13WeekPriceReturnDaily")
    private Double quarterYearDailyReturn;
    @JsonProperty("10DayAverageTradingVolume")
    private Double tenDayAverageTradingVolume;
    @JsonProperty("52WeekHigh")
    private Double yearHigh;
    @JsonProperty("52WeekHighDate")
    private LocalDate yearHighDate;
    @JsonProperty("52WeekLow")
    private Double yearLow;
    @JsonProperty("52WeekLowDate")
    private LocalDate yearLowDate;

}
