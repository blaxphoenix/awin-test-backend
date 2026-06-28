package com.example.awintestbackend.user.service;

import com.example.awintestbackend.user.repository.UserAdapter;
import com.example.awintestbackend.user.repository.UserRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserAdapter userAdapter;

    public UserServiceImpl(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    @Override
    public UserServiceDto createUser(UserServiceDto user) {
        log.info("Creating user with name: {}", user.name());
        UserRepositoryDto repoDto = toRepoDto(user);
        UserRepositoryDto savedRepoDto = userAdapter.save(repoDto);
        return toServiceDto(savedRepoDto);
    }

    @Override
    public Optional<UserServiceDto> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userAdapter.findById(id).map(this::toServiceDto);
    }

    @Override
    public List<UserServiceDto> getAllUsers() {
        log.info("Fetching all users");
        return userAdapter.findAll().stream()
                .map(this::toServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserServiceDto updateUser(Long id, UserServiceDto user) {
        log.info("Updating user with id: {}", id);
        UserRepositoryDto repoDto = new UserRepositoryDto(id, user.name(), user.email());
        UserRepositoryDto updatedRepoDto = userAdapter.save(repoDto);
        return toServiceDto(updatedRepoDto);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        userAdapter.deleteById(id);
    }

    private UserRepositoryDto toRepoDto(UserServiceDto dto) {
        return new UserRepositoryDto(dto.userid(), dto.name(), dto.email());
    }

    private UserServiceDto toServiceDto(UserRepositoryDto dto) {
        return new UserServiceDto(dto.userid(), dto.name(), dto.email());
    }
}
