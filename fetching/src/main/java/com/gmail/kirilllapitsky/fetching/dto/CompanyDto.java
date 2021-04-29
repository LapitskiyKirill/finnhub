package com.gmail.kirilllapitsky.fetching.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {
    private String country;
    private String currency;
    private String exchange;
    private String finnhubIndustry;
    private String ipo;
    private String logo;
    private String name;
    private String ticker;
    private String webUrl;
    private String displaySymbol;
    private String description;
}
