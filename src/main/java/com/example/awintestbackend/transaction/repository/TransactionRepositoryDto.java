package com.example.awintestbackend.transaction.repository;

import java.time.LocalDate;

public record TransactionRepositoryDto(
        Long id,
        Long userid,
        Double value,
        String details,
        LocalDate date
) {
}
