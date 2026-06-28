package com.example.awintestbackend.todo.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoControllerDto(
        Long id,
        @NotNull(message = "User ID is mandatory")
        Long userid,
        @NotBlank(message = "Description is mandatory")
        String description,
        String icon,
        boolean state
) {
}
