package com.example.awintestbackend.user.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserControllerDto(
        Long userid,
        @NotBlank(message = "Name is mandatory")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,
        @NotBlank(message = "Email is mandatory")
        @Email(message = "Email should be valid")
        String email,
        @NotBlank(message = "Currency is mandatory")
        @Size(min = 3, max = 3, message = "Currency must be 3 characters")
        String currency
) {
}
