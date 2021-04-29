package com.gmail.kirilllapitsky.finnhub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company")
public class Company {
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("country")
    @Column(name = "country")
    private String country;
    @JsonProperty("currency")
    @Column(name = "currency")
    private String currency;
    @JsonProperty("exchange")
    @Column(name = "exchange")
    private String exchange;
    @JsonProperty("finnhubIndustry")
    @Column(name = "finnhub_industry")
    private String finnhubIndustry;
    @JsonProperty("ipo")
    @Column(name = "ipo")
    private String ipo;
    @JsonProperty("logo")
    @Column(name = "logo")
    private String logo;
    @JsonProperty("name")
    @Column(name = "name")
    private String name;
    @JsonProperty("ticker")
    @Column(name = "ticker")
    private String ticker;
    @JsonProperty("webUrl")
    @Column(name = "web_url")
    private String webUrl;
    @JsonProperty("displaySymbol")
    @Column(name = "display_symbol")
    private String displaySymbol;
    @JsonProperty("description")
    @Column(name = "description")
    private String description;
}