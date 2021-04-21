package com.gmail.kirilllapitsky.finnhub;

import com.gmail.kirilllapitsky.finnhub.dto.*;
import com.gmail.kirilllapitsky.finnhub.entity.*;
import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static FinnhubCompanyMetrics getFinnhubCompanyMetrics(CompanyMetrics companyMetrics) {
        return FinnhubCompanyMetrics
                .builder()
                .finnhubMetrics(
                        FinnhubMetrics
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
                .role(Role.GUEST)
                .shouldBeRenew(false)
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

    public static List<FinnhubCompany> getFinnhubCompanies() {
        List<FinnhubCompany> parsedCompanies = new ArrayList<>();
        parsedCompanies.add(getFinnhubCompany());
        return parsedCompanies;
    }

    public static FinnhubCompany getFinnhubCompany() {
        return FinnhubCompany
                .builder()
                .currency("str")
                .description("str")
                .displaySymbol("str")
                .build();
    }

    public static FinnhubCompanyInfo getFinnhubCompanyInfoByCompany(Company company) {
        return FinnhubCompanyInfo
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

    public static FinnhubStockData getFinnhubStockData(StockData stockData) {
        return FinnhubStockData
                .builder()
                .currentPrice(stockData.getCurrentPrice())
                .openPrice(stockData.getOpenPrice())
                .build();
    }

    public static FinnhubStockData getFinnhubStockData(DailyStockData dailyStockData) {
        return FinnhubStockData
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

    public static List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(User
                    .builder()
                    .id(Long.valueOf(i))
                    .email("mail@gmail.com")
                    .username("userName")
                    .password("password")
                    .build());
        }

        return users;
    }

    public static List<Subscription> getSubscriptions(List<User> users) {
        return users
                .stream()
                .map(user -> Subscription
                        .builder()
                        .role(Role.MIDDLE)
                        .user(user)
                        .endDate(LocalDate.now().plusDays(7))
                        .shouldBeRenew(false)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<Subscription> getExpiringSubscriptions(List<User> users) {
        return users
                .stream()
                .map(user -> Subscription
                        .builder()
                        .role(Role.MIDDLE)
                        .user(user)
                        .endDate(LocalDate.now().minusDays(1))
                        .shouldBeRenew(false)
                        .build())
                .collect(Collectors.toList());
    }

    public static List<String> getEmails() {
        List<String> emails = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            emails.add("email" + i + "@gmail.com");
        }
        return emails;
    }


    public static MimeMessagePreparator getMimeMessagePreparator(String email, String message) {
        MimeMessagePreparator mimeMessagePreparator =
                mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
                    messageHelper.setFrom("projectMail");
                    messageHelper.setTo(email);
                    messageHelper.setSubject("FinnhubProject notification.");
                    messageHelper.setText(message);
                };
        return mimeMessagePreparator;
    }
}
