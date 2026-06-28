package com.example.awintestbackend.transaction.service;

import java.time.LocalDate;

public record TransactionServiceDto(
        Long id,
        Long userid,
        Double value,
        String details,
        LocalDate date
) {
}
