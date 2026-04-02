package com.latamholidays.service;

import com.latamholidays.dto.BusinessDayResponseDTO;
import com.latamholidays.dto.NextBusinessDayDTO;
import com.latamholidays.dto.BusinessDaysBetweenDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Service
public class BusinessDayService {

    private final HolidayService holidayService;

    public BusinessDayService(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public BusinessDayResponseDTO check(String country, LocalDate date, boolean includeSaturdays) {

        DayOfWeek dow = date.getDayOfWeek();

        if (dow == DayOfWeek.SUNDAY) {
            return new BusinessDayResponseDTO(
                    date.toString(), country, false, "WEEKEND", null
            );
        }

        if (dow == DayOfWeek.SATURDAY && !includeSaturdays) {
            return new BusinessDayResponseDTO(
                    date.toString(), country, false, "WEEKEND", null
            );
        }

        Set<LocalDate> holidays = holidayService.getHolidayDates(country, date.getYear());

        if (holidays.contains(date)) {
            String holidayName = holidayService
                    .getHolidays(country, date.getYear(), null, null)
                    .stream()
                    .filter(h -> h.date().equals(date.toString()))
                    .map(h -> h.nameEs())
                    .findFirst()
                    .orElse(null);

            return new BusinessDayResponseDTO(
                    date.toString(), country, false, "HOLIDAY", holidayName
            );
        }

        return new BusinessDayResponseDTO(
                date.toString(), country, true, null, null
        );
    }

    public NextBusinessDayDTO nextBusinessDay(String country, LocalDate from, int skip) {

        LocalDate cursor = from.plusDays(1);
        int count = 0;

        while (count < skip) {
            if (isBusinessDay(cursor, country, false)) {
                count++;
            }
            if (count < skip) {
                cursor = cursor.plusDays(1);
            }
        }

        return new NextBusinessDayDTO(
                from.toString(),
                cursor.toString(),
                skip
        );
    }

    public BusinessDaysBetweenDTO countBetween(String country, LocalDate start, LocalDate end, boolean includeSaturdays) {

        List<String> holidaysInRange = holidayService
                .getHolidays(country, start.getYear(), null, null)
                .stream()
                .filter(h -> {
                    LocalDate d = LocalDate.parse(h.date());
                    return !d.isBefore(start) && !d.isAfter(end);
                })
                .map(h -> h.date() + " - " + h.nameEs())
                .toList();

        int businessDays = (int) start.datesUntil(end.plusDays(1))
                .filter(d -> isBusinessDay(d, country, includeSaturdays))
                .count();

        int totalDays = (int) start.datesUntil(end.plusDays(1)).count();

        return new BusinessDaysBetweenDTO(
                start.toString(),
                end.toString(),
                businessDays,
                totalDays,
                holidaysInRange
        );
    }

    private boolean isBusinessDay(LocalDate date, String country, boolean includeSaturdays) {
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SUNDAY) {
            return false;
        }
        if (dow == DayOfWeek.SATURDAY && !includeSaturdays) {
            return false;
        }
        return !holidayService.getHolidayDates(country, date.getYear()).contains(date);
    }
}
