package com.example.awintestbackend.todo.service;

public record TodoServiceDto(
        Long id,
        Long userid,
        String description,
        String icon,
        boolean state
) {
}
