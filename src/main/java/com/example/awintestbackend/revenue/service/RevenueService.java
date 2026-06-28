package com.example.awintestbackend.revenue.service;

public interface RevenueService {
    RevenueServiceDto getTotalRevenue(Long userId, int days);
}
