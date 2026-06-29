package com.example.awintestbackend.revenue;

import com.example.awintestbackend.revenue.controller.RevenueControllerDto;
import com.example.awintestbackend.revenue.service.RevenueData;
import com.example.awintestbackend.revenue.service.RevenueTrendData;
import org.mapstruct.Mapper;

@Mapper
public interface RevenueMapper {

    // Controller <-> Service
    RevenueData toData(RevenueControllerDto dto);
    RevenueControllerDto toControllerDto(RevenueData data);
    RevenueControllerDto.RevenueTrendControllerDto toControllerDto(RevenueTrendData data);
    RevenueControllerDto.DailyRevenueControllerDto toControllerDto(RevenueTrendData.DailyRevenueData data);
}
