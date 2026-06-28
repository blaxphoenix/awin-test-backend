package com.example.awintestbackend.revenue.service;

public interface RevenueService {
    RevenueData getTotalRevenue(Long userId, int days);
}
