package com.example.awintestbackend.todo.repository;

import com.example.awintestbackend.user.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    private UserEntity createTestUser() {
        UserEntity user = new UserEntity(null, "John Doe", "john@example.com");
        return entityManager.persistAndFlush(user);
    }

    @Test
    void save_ShouldPersistTodo() {
        UserEntity user = createTestUser();
        TodoEntity todo = new TodoEntity(null, user.getUserid(), "Test Task", "icon", false);
        TodoEntity savedTodo = todoRepository.save(todo);

        assertNotNull(savedTodo.getId());
        TodoEntity foundTodo = entityManager.find(TodoEntity.class, savedTodo.getId());
        assertNotNull(foundTodo);
        assertEquals(todo.getDescription(), foundTodo.getDescription());
        assertEquals(todo.getUserid(), foundTodo.getUserid());
    }

    @Test
    void findByUserid_ShouldReturnTodos() {
        UserEntity user1 = createTestUser();
        UserEntity user2 = new UserEntity(null, "Jane Doe", "jane@example.com");
        entityManager.persistAndFlush(user2);

        entityManager.persist(new TodoEntity(null, user1.getUserid(), "Task 1", "icon1", false));
        entityManager.persist(new TodoEntity(null, user1.getUserid(), "Task 2", "icon2", true));
        entityManager.persist(new TodoEntity(null, user2.getUserid(), "Task 3", "icon3", false));
        entityManager.flush();

        List<TodoEntity> todos = todoRepository.findByUserid(user1.getUserid());

        assertEquals(2, todos.size());
    }

    @Test
    void findById_ShouldReturnTodo() {
        UserEntity user = createTestUser();
        TodoEntity todo = new TodoEntity(null, user.getUserid(), "Task 1", "icon1", false);
        TodoEntity persistedTodo = entityManager.persistAndFlush(todo);

        Optional<TodoEntity> found = todoRepository.findById(persistedTodo.getId());

        assertTrue(found.isPresent());
        assertEquals(todo.getDescription(), found.get().getDescription());
    }

    @Test
    void deleteById_ShouldRemoveTodo() {
        UserEntity user = createTestUser();
        TodoEntity todo = new TodoEntity(null, user.getUserid(), "Delete Me", "icon", false);
        TodoEntity persistedTodo = entityManager.persistAndFlush(todo);

        todoRepository.deleteById(persistedTodo.getId());
        entityManager.flush();
        entityManager.clear();

        TodoEntity found = entityManager.find(TodoEntity.class, persistedTodo.getId());
        assertNull(found);
    }

    @Test
    void deleteByUserid_ShouldRemoveAllUserTodos() {
        UserEntity user1 = createTestUser();
        UserEntity user2 = new UserEntity(null, "Jane Doe", "jane@example.com");
        entityManager.persistAndFlush(user2);

        entityManager.persist(new TodoEntity(null, user1.getUserid(), "Task 1", "icon1", false));
        entityManager.persist(new TodoEntity(null, user1.getUserid(), "Task 2", "icon2", true));
        entityManager.persist(new TodoEntity(null, user2.getUserid(), "Task 3", "icon3", false));
        entityManager.flush();

        todoRepository.deleteByUserid(user1.getUserid());
        entityManager.flush();
        entityManager.clear();

        List<TodoEntity> user1Todos = todoRepository.findByUserid(user1.getUserid());
        assertTrue(user1Todos.isEmpty());

        List<TodoEntity> user2Todos = todoRepository.findByUserid(user2.getUserid());
        assertEquals(1, user2Todos.size());
    }
}
