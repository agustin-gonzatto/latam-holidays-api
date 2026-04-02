package com.latamholidays.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "holidays", indexes = @Index(columnList = "country_code, date"))
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "name_es", nullable = false, length = 150)
    private String nameEs;

    @Column(name = "name_en", length = 150)
    private String nameEn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private HolidayType type;

    @Column(name = "is_banking")
    private boolean isBanking;

    @Column(name = "region_code", length = 10)
    private String regionCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
