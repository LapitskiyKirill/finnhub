package com.gmail.kirilllapitsky.finnhub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Deprecated
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stock_data")
public class StockData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(name = "current_price")
    private Double currentPrice;
    @Column(name = "open_price")
    private Double openPrice;
    @Column(name = "track_time")
    private LocalDateTime trackTime;

    @PrePersist
    public void onPrePersist() {
        trackTime = LocalDateTime.now();
    }
}
