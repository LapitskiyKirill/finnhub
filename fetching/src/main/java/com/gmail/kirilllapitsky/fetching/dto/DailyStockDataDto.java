package com.gmail.kirilllapitsky.fetching.dto;

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
