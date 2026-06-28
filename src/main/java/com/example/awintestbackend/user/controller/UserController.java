package com.example.awintestbackend.user.controller;

import com.example.awintestbackend.user.service.UserService;
import com.example.awintestbackend.user.service.UserServiceDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/u2m/v{version}/users", version = "1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserControllerDto> createUser(@Valid @RequestBody UserControllerDto userDto) {
        UserServiceDto serviceDto = toServiceDto(userDto);
        UserServiceDto createdUser = userService.createUser(serviceDto);
        return new ResponseEntity<>(toControllerDto(createdUser), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserControllerDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(this::toControllerDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<UserControllerDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(this::toControllerDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserControllerDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserControllerDto userDto) {
        UserServiceDto serviceDto = toServiceDto(userDto);
        UserServiceDto updatedUser = userService.updateUser(id, serviceDto);
        return ResponseEntity.ok(toControllerDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private UserServiceDto toServiceDto(UserControllerDto dto) {
        return new UserServiceDto(dto.userid(), dto.name(), dto.email(), dto.currency());
    }

    private UserControllerDto toControllerDto(UserServiceDto dto) {
        return new UserControllerDto(dto.userid(), dto.name(), dto.email(), dto.currency());
    }
}
