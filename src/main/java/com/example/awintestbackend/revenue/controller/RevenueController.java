package com.example.awintestbackend.revenue.controller;

import com.example.awintestbackend.revenue.RevenueMapper;
import com.example.awintestbackend.revenue.service.RevenueData;
import com.example.awintestbackend.revenue.service.RevenueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/u2m/v{version}/revenue", version = "1")
public class RevenueController {
    private final RevenueService revenueService;
    private final RevenueMapper revenueMapper;

    public RevenueController(RevenueService revenueService, RevenueMapper revenueMapper) {
        this.revenueService = revenueService;
        this.revenueMapper = revenueMapper;
    }

    @GetMapping
    public ResponseEntity<RevenueControllerDto> getTotalRevenue(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "30") int days) {
        RevenueData serviceDto = revenueService.getTotalRevenue(userId, days);
        return ResponseEntity.ok(revenueMapper.toControllerDto(serviceDto));
    }
}
