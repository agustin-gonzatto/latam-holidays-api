package com.latamholidays.repository;

import com.latamholidays.model.ApiUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiUsageRepository extends JpaRepository<ApiUsage, String> {
}
