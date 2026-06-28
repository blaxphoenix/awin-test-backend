package com.example.awintestbackend.user.repository;

public record UserRepositoryDto(
        Long userid,
        String name,
        String email,
        String currency
) {
}
