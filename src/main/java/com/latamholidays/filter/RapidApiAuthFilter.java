package com.latamholidays.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latamholidays.exception.RateLimitExceededException;
import com.latamholidays.service.RateLimitService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class RapidApiAuthFilter implements Filter {

    private final RateLimitService rateLimitService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${rapidapi.proxy-secret}")
    private String proxySecret;

    public RapidApiAuthFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String secret = req.getHeader("X-RapidAPI-Proxy-Secret");
        if (secret == null || !secret.equals(proxySecret)) {
            writeError(res, 403, "Forbidden", "Acceso directo no permitido. Usá RapidAPI.");
            return;
        }

        String apiKey = req.getHeader("X-RapidAPI-Key");
        if (apiKey == null) {
            writeError(res, 403, "Forbidden", "API key no encontrada.");
            return;
        }

        try {
            rateLimitService.check(apiKey);
        } catch (RateLimitExceededException e) {
            writeError(res, 429, "Too Many Requests", "Límite del plan excedido. Considerá hacer upgrade.");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/api/v1/countries")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/actuator");
    }

    private void writeError(HttpServletResponse res,
            int status,
            String error,
            String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json");
        res.getWriter().write(
                objectMapper.writeValueAsString(Map.of(
                        "status", status,
                        "error", error,
                        "message", message,
                        "timestamp", LocalDateTime.now().toString()
                ))
        );
    }
}
