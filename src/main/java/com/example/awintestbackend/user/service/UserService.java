package com.example.awintestbackend.user.service;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserServiceDto createUser(UserServiceDto user);

    Optional<UserServiceDto> getUserById(Long id);

    List<UserServiceDto> getAllUsers();

    UserServiceDto updateUser(Long id, UserServiceDto user);

    void deleteUser(Long id);
}
