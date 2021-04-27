package com.gmail.kirilllapitsky.fetching.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "daily_stock_data")
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

    @PrePersist
    public void onPrePersist() {
        trackTime = LocalDateTime.now();
    }
}
