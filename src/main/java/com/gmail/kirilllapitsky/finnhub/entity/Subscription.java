package com.gmail.kirilllapitsky.finnhub.entity;

import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"subscription\"")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne
    @MapsId
    private User user;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
}