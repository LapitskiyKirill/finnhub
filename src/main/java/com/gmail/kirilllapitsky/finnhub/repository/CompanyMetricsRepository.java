package com.gmail.kirilllapitsky.finnhub.repository;

import com.gmail.kirilllapitsky.finnhub.entity.Company;
import com.gmail.kirilllapitsky.finnhub.entity.CompanyMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyMetricsRepository extends JpaRepository<CompanyMetrics, Long> {
    Optional<CompanyMetrics> findByCompany(Company company);
}
