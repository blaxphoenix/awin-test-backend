package com.example.awintestbackend.user.repository;

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
class UserAdapterTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdapter userAdapter;

    private UserRepositoryDto repoDto;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        repoDto = new UserRepositoryDto(1L, "John Doe", "john.doe@example.com", "EUR");
        userEntity = new UserEntity(1L, "John Doe", "john.doe@example.com", "EUR");
    }

    @Test
    void save_ShouldReturnSavedDto() {
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserRepositoryDto result = userAdapter.save(repoDto);

        assertNotNull(result);
        assertEquals(repoDto.userid(), result.userid());
        assertEquals(repoDto.name(), result.name());
        assertEquals(repoDto.email(), result.email());
        assertEquals(repoDto.currency(), result.currency());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void findById_WhenUserExists_ShouldReturnDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        Optional<UserRepositoryDto> result = userAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(repoDto.userid(), result.get().userid());
        assertEquals(repoDto.name(), result.get().name());
        assertEquals(repoDto.email(), result.get().email());
        assertEquals(repoDto.currency(), result.get().currency());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserRepositoryDto> result = userAdapter.findById(1L);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfDtos() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserRepositoryDto> result = userAdapter.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(repoDto.userid(), result.getFirst().userid());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(userRepository).deleteById(1L);

        userAdapter.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
