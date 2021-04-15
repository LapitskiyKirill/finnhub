package com.gmail.kirilllapitsky.finnhub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "\"stock_data\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockData {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Company company;
    @Column(name = "current_price")
    private Double currentPrice;
    @Column(name = "high_price")
    private Double highPrice;
    @Column(name = "low_price")
    private Double lowPrice;
    @Column(name = "open_price")
    private Double openPrice;
    @Column(name = "previous_close_price")
    private Double previousClosePrice;
    @Column(name = "daily_min_percentage_change")
    private Double dailyMinPercentageChange;
    @Column(name = "daily_max_percentage_change")
    private Double dailyMaxPercentageChange;

}
