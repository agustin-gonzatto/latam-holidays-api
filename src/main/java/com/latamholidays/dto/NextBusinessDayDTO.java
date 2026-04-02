package com.latamholidays.dto;

public record NextBusinessDayDTO(
    String from,
    String nextBusinessDay,
    int skipped
) {}