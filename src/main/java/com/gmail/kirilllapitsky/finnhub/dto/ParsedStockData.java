package com.gmail.kirilllapitsky.finnhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParsedStockData {
    @JsonProperty("c")
    private Double currentPrice;
    @JsonProperty("h")
    private Double highPrice;
    @JsonProperty("l")
    private Double lowPrice;
    @JsonProperty("o")
    private Double openPrice;
    @JsonProperty("p")
    private Double previousClosePrice;
}