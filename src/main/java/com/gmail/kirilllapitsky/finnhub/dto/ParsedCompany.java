package com.gmail.kirilllapitsky.finnhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCompany {
    @JsonProperty("displaySymbol")
    private String displaySymbol;
    @JsonProperty("description")
    private String description;
    @JsonProperty("currency")
    private String currency;
}