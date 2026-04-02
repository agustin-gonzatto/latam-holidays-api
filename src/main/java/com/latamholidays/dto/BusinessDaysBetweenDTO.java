package com.latamholidays.dto;

public record BusinessDaysBetweenDTO(
    String start,
    String end,
    int businessDays,
    int totalDays,
    java.util.List<String> holidays
) {}