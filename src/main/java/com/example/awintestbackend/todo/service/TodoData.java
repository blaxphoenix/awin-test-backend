package com.example.awintestbackend.todo.service;

public record TodoData(
        Long id,
        Long userid,
        String description,
        String icon,
        boolean state
) {
}
