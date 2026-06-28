package com.example.awintestbackend.revenue.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.exception.GlobalExceptionHandler;
import com.example.awintestbackend.revenue.service.RevenueService;
import com.example.awintestbackend.revenue.service.RevenueServiceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RevenueController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class RevenueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RevenueService revenueService;

    @Test
    void getTotalRevenue_ShouldReturnRevenue() throws Exception {
        when(revenueService.getTotalRevenue(1L, 7)).thenReturn(new RevenueServiceDto(500.0));
    
        mockMvc.perform(get("/u2m/v1/revenue")
                        .param("userId", "1")
                        .param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue").value(500.0));
    }
    
    @Test
    void getTotalRevenue_WithDefaultValue_ShouldReturnRevenue() throws Exception {
        when(revenueService.getTotalRevenue(1L, 30)).thenReturn(new RevenueServiceDto(1000.0));
    
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
}
