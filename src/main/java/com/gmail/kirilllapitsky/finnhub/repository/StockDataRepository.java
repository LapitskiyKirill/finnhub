package com.gmail.kirilllapitsky.finnhub.repository;

import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.StockData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Deprecated
@Repository
public interface StockDataRepository extends JpaRepository<StockData, Long> {
    List<StockData> findAllByCompany(Company company, Pageable pageable);
}

