package com.example.awintestbackend.revenue.service;

import com.example.awintestbackend.revenue.repository.RevenueTrendRepositoryDto;
import com.example.awintestbackend.transaction.repository.TransactionAdapter;
import com.example.awintestbackend.transaction.repository.TransactionRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class RevenueServiceImpl implements RevenueService {

    private final TransactionAdapter transactionAdapter;

    public RevenueServiceImpl(TransactionAdapter transactionAdapter) {
        this.transactionAdapter = transactionAdapter;
    }

    @Override
    public RevenueData getTotalRevenue(Long userId, int days) {
        log.info("Calculating total revenue for user {} for the past {} days", userId, days);
        OffsetDateTime startDate = OffsetDateTime.now().minusDays(days);
        List<TransactionRepositoryDto> transactions = transactionAdapter.findByUseridAndDateGreaterThanEqual(userId, startDate);

        double totalRevenue = transactions.stream()
                .mapToDouble(TransactionRepositoryDto::value)
                .sum();

        return new RevenueData(totalRevenue);
    }

    @Override
    public RevenueTrendData getRevenueTrend(Long userId, int days) {
        log.info("Calculating revenue trend for user {} for the past {} days", userId, days);
        OffsetDateTime startDate = OffsetDateTime.now().minusDays(days);
        List<RevenueTrendRepositoryDto> trend = transactionAdapter.findDailyRevenueTrend(userId, startDate);

        List<RevenueTrendData.DailyRevenueData> dailyData = trend.stream()
                .map(dto -> new RevenueTrendData.DailyRevenueData(dto.date(), dto.totalRevenue()))
                .toList();

        return new RevenueTrendData(dailyData);
    }
}
