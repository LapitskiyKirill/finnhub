package com.gmail.kirilllapitsky.fetching.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinnhubCompanyMetrics {
    @JsonProperty("metric")
    private FinnhubMetrics finnhubMetrics;
}