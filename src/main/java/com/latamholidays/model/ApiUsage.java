package com.latamholidays.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "api_usage")
public class ApiUsage {

    @Id
    @Column(name = "api_key_hash", length = 64)
    private String apiKeyHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Plan plan = Plan.FREE;

    @Column(name = "requests_today")
    private int requestsToday;

    @Column(name = "requests_month")
    private int requestsMonth;

    @Column(name = "last_request_at")
    private LocalDateTime lastRequestAt;

    @Column(name = "reset_day_at")
    private LocalDate resetDayAt;

    @Column(name = "reset_month_at")
    private LocalDate resetMonthAt;

}
