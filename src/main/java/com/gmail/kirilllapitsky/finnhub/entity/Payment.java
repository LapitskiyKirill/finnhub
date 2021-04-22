package com.gmail.kirilllapitsky.finnhub.entity;

import com.gmail.kirilllapitsky.finnhub.security.enumerable.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "id")
    private String paymentId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "subscription_level")
    private Role subscriptionLevel;
    @Column(name = "is_completed")
    private Boolean isCompleted;

}
