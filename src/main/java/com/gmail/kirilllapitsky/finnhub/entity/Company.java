package com.gmail.kirilllapitsky.finnhub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "\"Company\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToMany(mappedBy = "trackingCompanies")
    private Set<User> trackingUsers = new HashSet<>();
    @OneToMany(mappedBy = "company")
    private Set<StockData> companyStockData = new HashSet<>();
    @OneToMany(mappedBy = "company")
    private Set<DailyStockData> dailyStockData = new HashSet<>();
}
