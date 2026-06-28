package com.example.awintestbackend.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDto(
        int status,
        String message,
        LocalDateTime timestamp,
        Map<String, String> errors
) {
    public ErrorResponseDto(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }

    public ErrorResponseDto(int status, String message, Map<String, String> errors) {
        this(status, message, LocalDateTime.now(), errors);
    }
}
