package com.latamholidays.dto;

public record BusinessDayResponseDTO(
    String date,
    String country,
    boolean isBusinessDay,
    String reason,
    String holidayName
) {}