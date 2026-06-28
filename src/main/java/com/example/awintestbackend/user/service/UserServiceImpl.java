package com.example.awintestbackend.user.service;

import com.example.awintestbackend.todo.service.TodoService;
import com.example.awintestbackend.transaction.service.TransactionService;
import com.example.awintestbackend.user.repository.UserAdapter;
import com.example.awintestbackend.user.repository.UserRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserAdapter userAdapter;
    private final TodoService todoService;
    private final TransactionService transactionService;

    public UserServiceImpl(UserAdapter userAdapter, TodoService todoService, TransactionService transactionService) {
        this.userAdapter = userAdapter;
        this.todoService = todoService;
        this.transactionService = transactionService;
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
        UserRepositoryDto repoDto = new UserRepositoryDto(id, user.name(), user.email(), user.currency());
        UserRepositoryDto updatedRepoDto = userAdapter.save(repoDto);
        return toServiceDto(updatedRepoDto);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        todoService.deleteTodosByUserid(id);
        transactionService.deleteTransactionsByUserid(id);
        userAdapter.deleteById(id);
    }

    private UserRepositoryDto toRepoDto(UserServiceDto dto) {
        return new UserRepositoryDto(dto.userid(), dto.name(), dto.email(), dto.currency());
    }

    private UserServiceDto toServiceDto(UserRepositoryDto dto) {
        return new UserServiceDto(dto.userid(), dto.name(), dto.email(), dto.currency());
    }
}
