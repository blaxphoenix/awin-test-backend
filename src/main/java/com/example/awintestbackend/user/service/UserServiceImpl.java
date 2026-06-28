package com.example.awintestbackend.user.service;

import com.example.awintestbackend.todo.service.TodoService;
import com.example.awintestbackend.transaction.service.TransactionService;
import com.example.awintestbackend.user.UserMapper;
import com.example.awintestbackend.user.repository.UserAdapter;
import com.example.awintestbackend.user.repository.UserRepositoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserAdapter userAdapter;
    private final TodoService todoService;
    private final TransactionService transactionService;
    private final UserMapper userMapper;

    public UserServiceImpl(UserAdapter userAdapter, TodoService todoService, TransactionService transactionService, UserMapper userMapper) {
        this.userAdapter = userAdapter;
        this.todoService = todoService;
        this.transactionService = transactionService;
        this.userMapper = userMapper;
    }

    @Override
    public UserData createUser(UserData user) {
        log.info("Creating user with name: {}", user.name());
        UserRepositoryDto repoDto = userMapper.toRepoDto(user);
        UserRepositoryDto savedRepoDto = userAdapter.save(repoDto);
        return userMapper.toData(savedRepoDto);
    }

    @Override
    public Optional<UserData> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        return userAdapter.findById(id).map(userMapper::toData);
    }

    @Override
    public List<UserData> getAllUsers() {
        log.info("Fetching all users");
        return userAdapter.findAll().stream()
                .map(userMapper::toData)
                .toList();
    }

    @Override
    public UserData updateUser(Long id, UserData user) {
        log.info("Updating user with id: {}", id);
        UserRepositoryDto repoDto = userMapper.toRepoDto(id, user);
        UserRepositoryDto updatedRepoDto = userAdapter.save(repoDto);
        return userMapper.toData(updatedRepoDto);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        todoService.deleteTodosByUserid(id);
        transactionService.deleteTransactionsByUserid(id);
        userAdapter.deleteById(id);
    }
}
