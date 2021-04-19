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
public class FinnhubCompany {
    @JsonProperty("displaySymbol")
    private String displaySymbol;
    @JsonProperty("description")
    private String description;
    @JsonProperty("currency")
    private String currency;
}