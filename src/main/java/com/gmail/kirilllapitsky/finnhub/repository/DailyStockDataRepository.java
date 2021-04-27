package com.gmail.kirilllapitsky.finnhub.repository;

import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.DailyStockData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Deprecated
@Repository
public interface DailyStockDataRepository extends JpaRepository<DailyStockData, Long> {
    List<DailyStockData> findAllByCompany(Company company, Pageable pageable);
}
