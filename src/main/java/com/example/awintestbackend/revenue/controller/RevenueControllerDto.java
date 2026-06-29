package com.example.awintestbackend.revenue.controller;

import java.time.LocalDate;
import java.util.List;

public record RevenueControllerDto(
        Double totalRevenue
) {
    public record RevenueTrendControllerDto(
            List<DailyRevenueControllerDto> trend
    ) {
    }

    public record DailyRevenueControllerDto(
            LocalDate date,
            Double totalRevenue
    ) {
    }
}
