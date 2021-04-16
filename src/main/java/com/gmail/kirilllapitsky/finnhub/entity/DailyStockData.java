package com.gmail.kirilllapitsky.finnhub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"daily_stock_data\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStockData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(name = "high_price")
    private Double highPrice;
    @Column(name = "low_price")
    private Double lowPrice;
    @Column(name = "track_time")
    private LocalDateTime trackTime;
}
