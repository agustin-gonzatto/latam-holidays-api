package com.latamholidays.service;

import com.latamholidays.dto.HolidayDTO;
import com.latamholidays.model.Holiday;
import com.latamholidays.repository.HolidayRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HolidayService {

    private final HolidayRepository repo;

    public HolidayService(HolidayRepository repo) {
        this.repo = repo;
    }

    @Cacheable(value = "holidays", key = "#country + '-' + #year")
    public List<HolidayDTO> getHolidays(String country, int year, String type, String region) {

        List<Holiday> holidays;

        if (region != null) {
            holidays = repo.findByCountryYearAndRegion(
                    country.toLowerCase(),
                    LocalDate.of(year, 1, 1),
                    LocalDate.of(year, 12, 31),
                    region.toUpperCase()
            );
        } else {
            holidays = repo.findByCountryAndYear(
                    country.toLowerCase(),
                    LocalDate.of(year, 1, 1),
                    LocalDate.of(year, 12, 31)
            );
        }

        return holidays.stream()
                .filter(h -> type == null || h.getType().name().equalsIgnoreCase(type))
                .map(this::toDTO)
                .toList();
    }

    @Cacheable(value = "holidays", key = "#country + '-' + #year + '-dates'")
    public Set<LocalDate> getHolidayDates(String country, int year) {
        return repo.findByCountryAndYear(
                country.toLowerCase(),
                LocalDate.of(year, 1, 1),
                LocalDate.of(year, 12, 31)
        )
                .stream()
                .map(Holiday::getDate)
                .collect(Collectors.toSet());
    }

    private HolidayDTO toDTO(Holiday h) {
        return new HolidayDTO(
                h.getDate().toString(),
                h.getNameEs(),
                h.getNameEn(),
                h.getType().name(),
                h.isBanking(),
                h.getRegionCode()
        );
    }
}
