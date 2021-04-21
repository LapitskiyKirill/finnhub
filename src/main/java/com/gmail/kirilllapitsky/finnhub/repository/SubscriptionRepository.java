package com.gmail.kirilllapitsky.finnhub.repository;

import com.gmail.kirilllapitsky.finnhub.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Query(value = "SELECT subscription FROM Subscription subscription where " +
            "(subscription.endDate = :endDate)")
    List<Subscription> findAllByEndDate(@Param("endDate") LocalDate endDate);

    @Query(value = "SELECT subscription FROM Subscription subscription where " +
            "(subscription.endDate = :endDate) and subscription.shouldBeRenew = :shouldBeRenew")
    List<Subscription> findAllByEndDateAndShouldBeRenew(@Param("endDate") LocalDate endDate,
                                                        @Param("shouldBeRenew") Boolean shouldBeRenew);
}
