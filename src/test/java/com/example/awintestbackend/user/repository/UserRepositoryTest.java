package com.example.awintestbackend.user.repository;

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
class UserRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_ShouldPersistUser() {
        UserEntity user = new UserEntity(null, "Jane Doe", "jane.doe@example.com", "USD");
        UserEntity savedUser = userRepository.save(user);

        assertNotNull(savedUser.getUserid());
        UserEntity foundUser = entityManager.find(UserEntity.class, savedUser.getUserid());
        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getCurrency(), foundUser.getCurrency());
    }

    @Test
    void findById_ShouldReturnUser() {
        UserEntity user = new UserEntity(null, "John Doe", "john.doe@example.com", "EUR");
        UserEntity persistedUser = entityManager.persistAndFlush(user);

        Optional<UserEntity> found = userRepository.findById(persistedUser.getUserid());

        assertTrue(found.isPresent());
        assertEquals(user.getName(), found.get().getName());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        entityManager.persist(new UserEntity(null, "User 1", "user1@example.com", "GBP"));
        entityManager.persist(new UserEntity(null, "User 2", "user2@example.com", "USD"));
        entityManager.flush();

        List<UserEntity> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        UserEntity user = new UserEntity(null, "Delete Me", "delete.me@example.com", "EUR");
        UserEntity persistedUser = entityManager.persistAndFlush(user);

        userRepository.deleteById(persistedUser.getUserid());
        entityManager.flush();
        entityManager.clear();

        UserEntity found = entityManager.find(UserEntity.class, persistedUser.getUserid());
        assertNull(found);
    }
}
