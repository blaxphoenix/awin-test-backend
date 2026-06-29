package com.example.awintestbackend.revenue.service;

import com.example.awintestbackend.revenue.repository.RevenueTrendRepositoryDto;
import com.example.awintestbackend.transaction.repository.TransactionAdapter;
import com.example.awintestbackend.transaction.repository.TransactionRepositoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RevenueServiceImplTest {

    @Mock
    private TransactionAdapter transactionAdapter;

    @InjectMocks
    private RevenueServiceImpl revenueService;

    @Test
    void getTotalRevenue_ShouldReturnSumOfTransactions() {
        Long userId = 100L;
        int days = 7;
        List<TransactionRepositoryDto> transactions = List.of(
                new TransactionRepositoryDto(1L, userId, 50.0, "Details 1", OffsetDateTime.now()),
                new TransactionRepositoryDto(2L, userId, 150.0, "Details 2", OffsetDateTime.now().minusDays(2))
        );

        when(transactionAdapter.findByUseridAndDateGreaterThanEqual(eq(userId), any(OffsetDateTime.class))).thenReturn(transactions);

        RevenueData result = revenueService.getTotalRevenue(userId, days);

        assertEquals(200.0, result.totalRevenue());
        verify(transactionAdapter, times(1)).findByUseridAndDateGreaterThanEqual(eq(userId), any(OffsetDateTime.class));
    }

    @Test
    void getTotalRevenue_WhenNoTransactions_ShouldReturnZero() {
        Long userId = 100L;
        when(transactionAdapter.findByUseridAndDateGreaterThanEqual(eq(userId), any(OffsetDateTime.class))).thenReturn(List.of());

        RevenueData result = revenueService.getTotalRevenue(userId, 30);

        assertEquals(0.0, result.totalRevenue());
    }

    @Test
    void getRevenueTrend_ShouldReturnDailyAggregatedRevenue() {
        Long userId = 100L;
        int days = 7;
        LocalDate today = LocalDate.now();
        List<RevenueTrendRepositoryDto> trendDtos = List.of(
                new RevenueTrendRepositoryDto(today.minusDays(2), 100.0),
                new RevenueTrendRepositoryDto(today.minusDays(1), 200.0),
                new RevenueTrendRepositoryDto(today, 50.0)
        );

        when(transactionAdapter.findDailyRevenueTrend(eq(userId), any(OffsetDateTime.class))).thenReturn(trendDtos);

        RevenueTrendData result = revenueService.getRevenueTrend(userId, days);

        assertEquals(3, result.trend().size());
        assertEquals(today.minusDays(2), result.trend().get(0).date());
        assertEquals(100.0, result.trend().get(0).totalRevenue());
        assertEquals(today.minusDays(1), result.trend().get(1).date());
        assertEquals(200.0, result.trend().get(1).totalRevenue());
        assertEquals(today, result.trend().get(2).date());
        assertEquals(50.0, result.trend().get(2).totalRevenue());
        verify(transactionAdapter, times(1)).findDailyRevenueTrend(eq(userId), any(OffsetDateTime.class));
    }

    @Test
    void getRevenueTrend_WhenNoTransactions_ShouldReturnEmptyList() {
        Long userId = 100L;
        when(transactionAdapter.findDailyRevenueTrend(eq(userId), any(OffsetDateTime.class))).thenReturn(List.of());

        RevenueTrendData result = revenueService.getRevenueTrend(userId, 30);

        assertEquals(0, result.trend().size());
    }
}
