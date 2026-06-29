package com.example.awintestbackend.revenue.service;

import java.time.LocalDate;
import java.util.List;

public record RevenueTrendData(
        List<DailyRevenueData> trend
) {
    public record DailyRevenueData(
            LocalDate date,
            Double totalRevenue
    ) {
    }
}
