package com.gmail.kirilllapitsky.finnhub.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DailyStockDataDto {
    private String companyName;
    private Double highPrice;
    private Double lowPrice;
    private LocalDateTime trackTime;
}
