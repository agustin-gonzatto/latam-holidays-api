package com.latamholidays.controller;

import com.latamholidays.dto.BusinessDayResponseDTO;
import com.latamholidays.dto.BusinessDaysBetweenDTO;
import com.latamholidays.dto.NextBusinessDayDTO;
import com.latamholidays.service.BusinessDayService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/{country}")
public class BusinessDayController {

    private final BusinessDayService businessDayService;

    public BusinessDayController(BusinessDayService businessDayService) {
        this.businessDayService = businessDayService;
    }

    @GetMapping("/is-business-day")
    public ResponseEntity<BusinessDayResponseDTO> isBusinessDay(
            @PathVariable String country,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "false") boolean includeSaturdays) {

        return ResponseEntity.ok(
                businessDayService.check(country, date, includeSaturdays)
        );
    }

    @GetMapping("/next-business-day")
    public ResponseEntity<NextBusinessDayDTO> nextBusinessDay(
            @PathVariable String country,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(defaultValue = "1") int skip) {

        return ResponseEntity.ok(
                businessDayService.nextBusinessDay(country, from, skip)
        );
    }

    @GetMapping("/business-days-between")
    public ResponseEntity<BusinessDaysBetweenDTO> businessDaysBetween(
            @PathVariable String country,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "false") boolean includeSaturdays) {

        return ResponseEntity.ok(
                businessDayService.countBetween(country, start, end, includeSaturdays)
        );
    }
}
