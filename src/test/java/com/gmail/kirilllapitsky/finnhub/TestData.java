package com.gmail.kirilllapitsky.finnhub;

import com.gmail.kirilllapitsky.finnhub.dto.*;
import com.gmail.kirilllapitsky.finnhub.entity.*;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestData {
    public static Company getCompany() {
        return Company
                .builder()
                .country("US")
                .currency("currency")
                .description("description")
                .displaySymbol("TICK")
                .exchange("exchange")
                .finnhubIndustry("finnhubIndustry")
                .ipo("ipo")
                .logo("logo")
                .name("name")
                .ticker("TICK")
                .webUrl("weburl")
                .build();
    }

    public static CompanyMetrics getCompanyMetrics() {
        return CompanyMetrics
                .builder()
                .yearLow(1.1)
                .yearHigh(2.2)
                .tenDayAverageTradingVolume(0.1)
                .quarterYearDailyReturn(0.1)
                .yearDailyReturn(0.1)
                .halfYearDailyReturn(0.1)
                .yearHighDate(LocalDate.of(2020, 8, 1))
                .yearLowDate(LocalDate.of(2020, 8, 2))
                .build();
    }

    public static ParsedCompanyMetrics getParsedCompanyMetrics(CompanyMetrics companyMetrics) {
        return ParsedCompanyMetrics
                .builder()
                .parsedMetrics(
                        ParsedMetrics
                                .builder()
                                .yearLow(companyMetrics.getYearLow())
                                .yearHigh(companyMetrics.getYearHigh())
                                .tenDayAverageTradingVolume(companyMetrics.getTenDayAverageTradingVolume())
                                .quarterYearDailyReturn(companyMetrics.getQuarterYearDailyReturn())
                                .yearDailyReturn(companyMetrics.getYearDailyReturn())
                                .halfYearDailyReturn(companyMetrics.getHalfYearDailyReturn())
                                .yearHighDate(companyMetrics.getYearHighDate())
                                .yearLowDate(companyMetrics.getYearLowDate())
                                .build()
                )
                .build();
    }

    public static DailyStockData getDailyStockData() {
        return DailyStockData
                .builder()
                .lowPrice(1.1)
                .highPrice(2.2)
                .build();
    }

    public static StockData getStockData() {
        return StockData
                .builder()
                .openPrice(2.2)
                .currentPrice(1.2)
                .build();
    }

    public static Subscription getSubscription() {
        return Subscription
                .builder()
                .role(Role.BEGINNER)
                .build();
    }

    public static User getUser() {
        return User
                .builder()
                .email("mail@gmail.com")
                .username("userName")
                .password("password")
                .build();
    }

    public static List<ParsedCompany> getParsedCompanies() {
        List<ParsedCompany> parsedCompanies = new ArrayList<>();
        parsedCompanies.add(getParsedCompany());
        return parsedCompanies;
    }

    public static ParsedCompany getParsedCompany() {
        return ParsedCompany
                .builder()
                .currency("str")
                .description("str")
                .displaySymbol("str")
                .build();
    }

    public static ParsedCompanyInfo getParsedCompanyInfoByCompany(Company company) {
        return ParsedCompanyInfo
                .builder()
                .country(company.getCountry())
                .exchange(company.getExchange())
                .finnhubIndustry(company.getFinnhubIndustry())
                .ipo(company.getIpo())
                .logo(company.getLogo())
                .name(company.getName())
                .ticker(company.getTicker())
                .weburl(company.getWebUrl())
                .build();
    }

    public static ParsedStockData getParsedStockData(StockData stockData) {
        return ParsedStockData
                .builder()
                .currentPrice(stockData.getCurrentPrice())
                .openPrice(stockData.getOpenPrice())
                .build();
    }

    public static ParsedStockData getParsedStockData(DailyStockData dailyStockData) {
        return ParsedStockData
                .builder()
                .highPrice(dailyStockData.getHighPrice())
                .lowPrice(dailyStockData.getLowPrice())
                .build();
    }

    public static RegisterRequest getRegisterRequest() {
        return RegisterRequest
                .builder()
                .email("e@mail.com")
                .password("password")
                .username("username")
                .build();
    }

    public static User getUserByRegisterRequest(RegisterRequest registerRequest) {
        return User.builder()
                .password(registerRequest.getPassword())
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();
    }
}
