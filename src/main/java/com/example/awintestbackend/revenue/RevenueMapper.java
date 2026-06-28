package com.example.awintestbackend.revenue;

import com.example.awintestbackend.revenue.controller.RevenueControllerDto;
import com.example.awintestbackend.revenue.service.RevenueData;
import org.mapstruct.Mapper;

@Mapper
public interface RevenueMapper {

    // Controller <-> Service
    RevenueData toData(RevenueControllerDto dto);
    RevenueControllerDto toControllerDto(RevenueData data);
}
