package com.example.awintestbackend.revenue.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.exception.GlobalExceptionHandler;
import com.example.awintestbackend.revenue.RevenueMapper;
import com.example.awintestbackend.revenue.service.RevenueData;
import com.example.awintestbackend.revenue.service.RevenueService;
import com.example.awintestbackend.revenue.service.RevenueTrendData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RevenueController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@WithMockUser
class RevenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RevenueService revenueService;

    @MockitoBean
    private RevenueMapper revenueMapper;

    @Test
    void getTotalRevenue_ShouldReturnRevenue() throws Exception {
        when(revenueService.getTotalRevenue(1L, 7)).thenReturn(new RevenueData(500.0));
        when(revenueMapper.toControllerDto(any(RevenueData.class))).thenReturn(new RevenueControllerDto(500.0));

        mockMvc.perform(get("/u2m/v1/revenue")
                        .param("userId", "1")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(500.0));
    }

    @Test
    void getTotalRevenue_WithDefaultValue_ShouldReturnRevenue() throws Exception {
        when(revenueService.getTotalRevenue(1L, 30)).thenReturn(new RevenueData(1000.0));
        when(revenueMapper.toControllerDto(any(RevenueData.class))).thenReturn(new RevenueControllerDto(1000.0));

        mockMvc.perform(get("/u2m/v1/revenue")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(1000.0));
    }

    @Test
    void getTotalRevenue_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/u2m/v1/revenue"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRevenueTrend_ShouldReturnDailyTrend() throws Exception {
        LocalDate today = LocalDate.now();
        RevenueTrendData trendData = new RevenueTrendData(List.of(
                new RevenueTrendData.DailyRevenueData(today.minusDays(1), 100.0),
                new RevenueTrendData.DailyRevenueData(today, 200.0)
        ));
        RevenueControllerDto.RevenueTrendControllerDto trendDto = new RevenueControllerDto.RevenueTrendControllerDto(List.of(
                new RevenueControllerDto.DailyRevenueControllerDto(today.minusDays(1), 100.0),
                new RevenueControllerDto.DailyRevenueControllerDto(today, 200.0)
        ));

        when(revenueService.getRevenueTrend(1L, 7)).thenReturn(trendData);
        when(revenueMapper.toControllerDto(any(RevenueTrendData.class))).thenReturn(trendDto);

        mockMvc.perform(get("/u2m/v1/revenue/trend")
                        .param("userId", "1")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trend").isArray())
                .andExpect(jsonPath("$.trend.length()").value(2))
                .andExpect(jsonPath("$.trend[0].totalRevenue").value(100.0))
                .andExpect(jsonPath("$.trend[1].totalRevenue").value(200.0));
    }

    @Test
    void getRevenueTrend_WithDefaultDays_ShouldReturnTrend() throws Exception {
        RevenueTrendData trendData = new RevenueTrendData(List.of());
        RevenueControllerDto.RevenueTrendControllerDto trendDto = new RevenueControllerDto.RevenueTrendControllerDto(List.of());

        when(revenueService.getRevenueTrend(1L, 30)).thenReturn(trendData);
        when(revenueMapper.toControllerDto(any(RevenueTrendData.class))).thenReturn(trendDto);

        mockMvc.perform(get("/u2m/v1/revenue/trend")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trend").isArray());
    }

    @Test
    void getRevenueTrend_WithoutUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/u2m/v1/revenue/trend"))
                .andExpect(status().isBadRequest());
    }
}
