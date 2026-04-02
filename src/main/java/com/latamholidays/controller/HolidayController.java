package com.latamholidays.controller;

import com.latamholidays.dto.HolidayDTO;
import com.latamholidays.service.HolidayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/{country}")
public class HolidayController {

    private final HolidayService holidayService;

    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    @GetMapping("/holidays/{year}")
    public ResponseEntity<List<HolidayDTO>> getHolidays(
            @PathVariable String country,
            @PathVariable int year,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String region) {

        return ResponseEntity.ok(
            holidayService.getHolidays(country, year, type, region)
        );
    }
}