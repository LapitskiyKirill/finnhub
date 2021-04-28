package com.gmail.kirilllapitsky.fetching.entity;

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
@Table(name = "finnhub_company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "country")
    private String country;
    @Column(name = "currency")
    private String currency;
    @Column(name = "exchange")
    private String exchange;
    @Column(name = "finnhub_industry")
    private String finnhubIndustry;
    @Column(name = "ipo")
    private String ipo;
    @Column(name = "logo")
    private String logo;
    @Column(name = "name")
    private String name;
    @Column(name = "ticker")
    private String ticker;
    @Column(name = "web_url")
    private String webUrl;
    @Column(name = "display_symbol")
    private String displaySymbol;
    @Column(name = "description")
    private String description;
}
