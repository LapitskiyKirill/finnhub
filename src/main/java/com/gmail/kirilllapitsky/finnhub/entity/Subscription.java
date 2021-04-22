package com.gmail.kirilllapitsky.finnhub.entity;

import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription")
public class Subscription {
    @Id
    @Column(name = "id")
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "role")
    private Role role;
    @Column(name = "should_be_renew")
    private Boolean shouldBeRenew;
    @Column(name = "renew_level")
    private Role renewLevel;

    @PrePersist
    public void onPrePersist() {
        startDate = LocalDate.now();
    }

}