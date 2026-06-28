package com.example.awintestbackend.user.service;

public record UserServiceDto(
        Long userid,
        String name,
        String email
) {
}
