package com.example.awintestbackend.user;

import com.example.awintestbackend.todo.repository.TodoEntity;
import com.example.awintestbackend.todo.repository.TodoRepository;
import com.example.awintestbackend.transaction.repository.TransactionEntity;
import com.example.awintestbackend.transaction.repository.TransactionRepository;
import com.example.awintestbackend.user.repository.UserEntity;
import com.example.awintestbackend.user.repository.UserRepository;
import com.example.awintestbackend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserDeletionTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    @Transactional
    void deleteUser_WithTodosAndTransactions_ShouldSucceed() {
        // Create user
        UserEntity user = new UserEntity(null, "Test User", "test@example.com", "EUR");
        UserEntity savedUser = userRepository.saveAndFlush(user);

        // Create to-do for user
        TodoEntity todo = new TodoEntity(null, savedUser.getUserid(), "Test Todo", "icon", false);
        todoRepository.saveAndFlush(todo);

        // Create transaction for user
        TransactionEntity transaction = new TransactionEntity(null, savedUser.getUserid(), 100.0, "Test Transaction", OffsetDateTime.now());
        transactionRepository.saveAndFlush(transaction);

        // Delete user via service
        userService.deleteUser(savedUser.getUserid());
        userRepository.flush();

        // Verify user, todos and transactions are gone
        assertNull(userRepository.findById(savedUser.getUserid()).orElse(null));
        assertTrue(todoRepository.findByUserid(savedUser.getUserid()).isEmpty());
        assertTrue(transactionRepository.findByUserid(savedUser.getUserid()).isEmpty());
    }
}
