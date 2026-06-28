package com.example.awintestbackend.user.service;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserData createUser(UserData user);

    Optional<UserData> getUserById(Long id);

    List<UserData> getAllUsers();

    UserData updateUser(Long id, UserData user);

    void deleteUser(Long id);
}
