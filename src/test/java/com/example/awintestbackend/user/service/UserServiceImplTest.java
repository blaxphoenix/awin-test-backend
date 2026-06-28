package com.example.awintestbackend.user.service;

import com.example.awintestbackend.user.repository.UserAdapter;
import com.example.awintestbackend.user.repository.UserRepositoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserAdapter userAdapter;

    @InjectMocks
    private UserServiceImpl userService;

    private UserServiceDto serviceDto;
    private UserRepositoryDto repoDto;

    @BeforeEach
    void setUp() {
        serviceDto = new UserServiceDto(1L, "John Doe", "john.doe@example.com");
        repoDto = new UserRepositoryDto(1L, "John Doe", "john.doe@example.com");
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        when(userAdapter.save(any(UserRepositoryDto.class))).thenReturn(repoDto);

        UserServiceDto result = userService.createUser(serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.userid(), result.userid());
        assertEquals(serviceDto.name(), result.name());
        assertEquals(serviceDto.email(), result.email());
        verify(userAdapter, times(1)).save(any(UserRepositoryDto.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userAdapter.findById(1L)).thenReturn(Optional.of(repoDto));

        Optional<UserServiceDto> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(serviceDto.userid(), result.get().userid());
        assertEquals(serviceDto.name(), result.get().name());
        assertEquals(serviceDto.email(), result.get().email());
        verify(userAdapter, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userAdapter.findById(1L)).thenReturn(Optional.empty());

        Optional<UserServiceDto> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userAdapter, times(1)).findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userAdapter.findAll()).thenReturn(List.of(repoDto));

        List<UserServiceDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(serviceDto.userid(), result.get(0).userid());
        verify(userAdapter, times(1)).findAll();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userAdapter.save(any(UserRepositoryDto.class))).thenReturn(repoDto);

        UserServiceDto result = userService.updateUser(1L, serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.userid(), result.userid());
        assertEquals(serviceDto.name(), result.name());
        assertEquals(serviceDto.email(), result.email());
        verify(userAdapter, times(1)).save(any(UserRepositoryDto.class));
    }

    @Test
    void deleteUser_ShouldCallAdapterDelete() {
        doNothing().when(userAdapter).deleteById(1L);

        userService.deleteUser(1L);

        verify(userAdapter, times(1)).deleteById(1L);
    }
}
