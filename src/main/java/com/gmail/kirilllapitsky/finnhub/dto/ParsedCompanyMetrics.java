package com.gmail.kirilllapitsky.finnhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCompanyMetrics {
    @JsonProperty("metric")
    private ParsedMetrics parsedMetrics;
}