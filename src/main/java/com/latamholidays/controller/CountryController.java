package com.latamholidays.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CountryController {

    @GetMapping("/countries")
    public ResponseEntity<List<Map<String, String>>> getCountries() {
        return ResponseEntity.ok(List.of(
            Map.of("code", "mx", "name", "México",     "timezone", "America/Mexico_City"),
            Map.of("code", "co", "name", "Colombia",   "timezone", "America/Bogota"),
            Map.of("code", "ar", "name", "Argentina",  "timezone", "America/Argentina/Buenos_Aires"),
            Map.of("code", "cl", "name", "Chile",      "timezone", "America/Santiago"),
            Map.of("code", "pe", "name", "Perú",       "timezone", "America/Lima")
        ));
    }
}