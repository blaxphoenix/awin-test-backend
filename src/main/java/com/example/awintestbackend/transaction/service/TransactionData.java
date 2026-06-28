package com.example.awintestbackend.transaction.service;

import java.time.OffsetDateTime;

public record TransactionData(
        Long id,
        Long userid,
        Double value,
        String details,
        OffsetDateTime date
) {
}
