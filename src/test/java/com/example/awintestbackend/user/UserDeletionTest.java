package com.example.awintestbackend.user;

import com.example.awintestbackend.todo.repository.TodoEntity;
import com.example.awintestbackend.todo.repository.TodoRepository;
import com.example.awintestbackend.user.repository.UserEntity;
import com.example.awintestbackend.user.repository.UserRepository;
import com.example.awintestbackend.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @Transactional
    void deleteUser_WithTodos_ShouldSucceed() {
        // Create user
        UserEntity user = new UserEntity(null, "Test User", "test@example.com");
        UserEntity savedUser = userRepository.saveAndFlush(user);

        // Create to-do for user
        TodoEntity todo = new TodoEntity(null, savedUser.getUserid(), "Test Todo", "icon", false);
        todoRepository.saveAndFlush(todo);

        // Delete user via service
        userService.deleteUser(savedUser.getUserid());
        userRepository.flush();

        // Verify user and todos are gone
        assertNull(userRepository.findById(savedUser.getUserid()).orElse(null));
        assertTrue(todoRepository.findByUserid(savedUser.getUserid()).isEmpty());
    }
}
