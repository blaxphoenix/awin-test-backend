package com.example.awintestbackend.todo.repository;

public record TodoRepositoryDto(
        Long id,
        Long userid,
        String description,
        String icon,
        boolean state
) {
}
