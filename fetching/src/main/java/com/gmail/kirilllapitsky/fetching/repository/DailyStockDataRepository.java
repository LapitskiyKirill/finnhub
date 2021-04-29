package com.gmail.kirilllapitsky.fetching.repository;

import com.gmail.kirilllapitsky.fetching.entity.Company;
import com.gmail.kirilllapitsky.fetching.entity.DailyStockData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyStockDataRepository extends JpaRepository<DailyStockData, Long> {
    List<DailyStockData> findAllByCompany(Company company, Pageable pageable);
}
