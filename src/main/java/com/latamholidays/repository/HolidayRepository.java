package com.latamholidays.repository;

import com.latamholidays.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT h FROM Holiday h WHERE h.countryCode = :country "
            + "AND h.date BETWEEN :start AND :end")
    List<Holiday> findByCountryAndYear(
            @Param("country") String country,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("SELECT h FROM Holiday h WHERE h.countryCode = :country "
            + "AND h.date BETWEEN :start AND :end")
    List<Holiday> findByCountryAndDateBetween(
            @Param("country") String country,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("SELECT h FROM Holiday h WHERE h.countryCode = :country "
            + "AND h.date BETWEEN :start AND :end "
            + "AND (h.regionCode IS NULL OR h.regionCode = :region)")
    List<Holiday> findByCountryYearAndRegion(
            @Param("country") String country,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end,
            @Param("region") String region
    );
}
