package com.example.awintestbackend.transaction.repository;

import java.time.OffsetDateTime;

public record TransactionRepositoryDto(
        Long id,
        Long userid,
        Double value,
        String details,
        OffsetDateTime date
) {
}
