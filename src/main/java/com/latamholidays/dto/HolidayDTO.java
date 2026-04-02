package com.latamholidays.dto;

public record HolidayDTO(
    String date,
    String nameEs,
    String nameEn,
    String type,
    boolean isBanking,
    String regionCode
) {}