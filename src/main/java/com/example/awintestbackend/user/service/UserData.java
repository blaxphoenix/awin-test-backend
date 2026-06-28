package com.example.awintestbackend.user.service;

public record UserData(
        Long userid,
        String name,
        String email,
        String currency
) {
}
