package com.gmail.kirilllapitsky.finnhub.repository;

import com.gmail.kirilllapitsky.finnhub.entity.StockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDataRepository extends JpaRepository<StockData, Long> {
}
