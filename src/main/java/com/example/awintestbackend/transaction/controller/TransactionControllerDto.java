package com.example.awintestbackend.transaction.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

public record TransactionControllerDto(
        Long id,
        @NotNull(message = "User ID is mandatory")
        Long userid,
        @NotNull(message = "Value is mandatory")
        Double value,
        @NotBlank(message = "Details are mandatory")
        String details,
        @NotNull(message = "Date is mandatory")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        OffsetDateTime date
) {
}
