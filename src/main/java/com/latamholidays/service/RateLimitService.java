package com.latamholidays.service;

import com.latamholidays.exception.RateLimitExceededException;
import com.latamholidays.model.ApiUsage;
import com.latamholidays.model.Plan;
import com.latamholidays.repository.ApiUsageRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
public class RateLimitService {

    private final ApiUsageRepository repo;

    public RateLimitService(ApiUsageRepository repo) {
        this.repo = repo;
    }

    public void check(String apiKey) {
        String hash = hash(apiKey);
        ApiUsage usage = repo.findById(hash).orElse(newFreeUsage(hash));

        resetIfNeeded(usage);

        int limit = limitForPlan(usage.getPlan());

        if (usage.getRequestsMonth() >= limit) {
            throw new RateLimitExceededException();
        }

        usage.setRequestsToday(usage.getRequestsToday() + 1);
        usage.setRequestsMonth(usage.getRequestsMonth() + 1);
        usage.setLastRequestAt(LocalDateTime.now());
        repo.save(usage);
    }

    private void resetIfNeeded(ApiUsage usage) {
        LocalDate today = LocalDate.now();

        // Reset diario
        if (usage.getResetDayAt().isBefore(today)) {
            usage.setRequestsToday(0);
            usage.setResetDayAt(today);
        }

        // Reset mensual
        LocalDate startOfMonth = today.withDayOfMonth(1);
        if (usage.getResetMonthAt().isBefore(startOfMonth)) {
            usage.setRequestsMonth(0);
            usage.setResetMonthAt(startOfMonth);
        }
    }

    private int limitForPlan(Plan plan) {
        return switch (plan) {
            case FREE ->
                500;
            case BASIC ->
                10_000;
            case PRO ->
                100_000;
            case ENTERPRISE ->
                Integer.MAX_VALUE;
        };
    }

    private ApiUsage newFreeUsage(String hash) {
        ApiUsage usage = new ApiUsage();
        usage.setApiKeyHash(hash);
        usage.setPlan(Plan.FREE);
        usage.setRequestsToday(0);
        usage.setRequestsMonth(0);
        usage.setResetDayAt(LocalDate.now());
        usage.setResetMonthAt(LocalDate.now().withDayOfMonth(1));
        return usage;
    }

    public String hash(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error hasheando API key", e);
        }
    }
}
