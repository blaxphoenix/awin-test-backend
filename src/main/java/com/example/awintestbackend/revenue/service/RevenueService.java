package com.example.awintestbackend.revenue.service;

public interface RevenueService {
    RevenueData getTotalRevenue(Long userId, int days);
    RevenueTrendData getRevenueTrend(Long userId, int days);
}
