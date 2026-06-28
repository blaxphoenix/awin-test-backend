package com.example.awintestbackend.revenue.controller;

import com.example.awintestbackend.revenue.service.RevenueService;
import com.example.awintestbackend.revenue.service.RevenueServiceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/u2m/v{version}/revenue", version = "1")
public class RevenueController {

    private final RevenueService revenueService;

    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public ResponseEntity<RevenueControllerDto> getTotalRevenue(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "30") int days) {
        RevenueServiceDto serviceDto = revenueService.getTotalRevenue(userId, days);
        return ResponseEntity.ok(new RevenueControllerDto(serviceDto.totalRevenue()));
    }
}
