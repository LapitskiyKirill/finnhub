package com.gmail.kirilllapitsky.fetching.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StockDataDto {
    private String companyName;
    private Double currentPrice;
    private Double openPrice;
    private LocalDateTime trackTime;
}
