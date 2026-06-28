package com.example.awintestbackend.revenue.service;

import com.example.awintestbackend.transaction.repository.TransactionAdapter;
import com.example.awintestbackend.transaction.repository.TransactionRepositoryDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
