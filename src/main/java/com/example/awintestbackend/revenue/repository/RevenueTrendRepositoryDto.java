package com.example.awintestbackend.revenue.repository;

import java.time.LocalDate;

public record RevenueTrendRepositoryDto(
        LocalDate date,
        Double totalRevenue
) {
}
