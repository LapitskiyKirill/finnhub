package com.gmail.kirilllapitsky.finnhub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@Deprecated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "company_metrics")
public class CompanyMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(name = "year_daily_return")
    private Double yearDailyReturn;
    @Column(name = "half_year_daily_return")
    private Double halfYearDailyReturn;
    @Column(name = "quarter_year_daily_return")
    private Double quarterYearDailyReturn;
    @Column(name = "ten_day_average_trading_volume")
    private Double tenDayAverageTradingVolume;
    @Column(name = "year_high")
    private Double yearHigh;
    @Column(name = "year_high_date")
    private LocalDate yearHighDate;
    @Column(name = "year_low")
    private Double yearLow;
    @Column(name = "year_low_date")
    private LocalDate yearLowDate;
}
