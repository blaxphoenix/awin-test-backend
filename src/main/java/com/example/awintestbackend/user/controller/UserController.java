package com.example.awintestbackend.user.controller;

import com.example.awintestbackend.exception.ResourceNotFoundException;
import com.example.awintestbackend.user.UserMapper;
import com.example.awintestbackend.user.service.UserData;
import com.example.awintestbackend.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/u2m/v{version}/users", version = "1")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserControllerDto> createUser(@Valid @RequestBody UserControllerDto userDto) {
        UserData serviceDto = userMapper.toData(userDto);
        UserData createdUser = userService.createUser(serviceDto);
        return new ResponseEntity<>(userMapper.toControllerDto(createdUser), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserControllerDto> getUserById(@PathVariable Long id) {
        UserControllerDto user = userService.getUserById(id)
                .map(userMapper::toControllerDto)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserControllerDto>> getAllUsers() {
        List<UserControllerDto> users = userService.getAllUsers().stream()
                .map(userMapper::toControllerDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserControllerDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserControllerDto userDto) {
        UserData serviceDto = userMapper.toData(userDto);
        UserData updatedUser = userService.updateUser(id, serviceDto);
        return ResponseEntity.ok(userMapper.toControllerDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
