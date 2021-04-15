package com.gmail.kirilllapitsky.finnhub.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCompanyInfo {
    @JsonProperty("country")
    private String country;
    @JsonProperty("exchange")
    private String exchange;
    @JsonProperty("finnhubIndustry")
    private String finnhubIndustry;
    @JsonProperty("ipo")
    private String ipo;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("name")
    private String name;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("shareOutstanding")
    private Double shareOutstanding;
    @JsonProperty("ticker")
    private String ticker;
    @JsonProperty("weburl")
    private String weburl;
}