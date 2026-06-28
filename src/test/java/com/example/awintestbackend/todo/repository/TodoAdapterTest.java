package com.example.awintestbackend.todo.repository;

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
class TodoAdapterTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoAdapter todoAdapter;

    private TodoRepositoryDto repoDto;
    private TodoEntity todoEntity;

    @BeforeEach
    void setUp() {
        repoDto = new TodoRepositoryDto(1L, 100L, "Task 1", "icon-1", false);
        todoEntity = new TodoEntity(1L, 100L, "Task 1", "icon-1", false);
    }

    @Test
    void save_ShouldReturnSavedDto() {
        when(todoRepository.save(any(TodoEntity.class))).thenReturn(todoEntity);

        TodoRepositoryDto result = todoAdapter.save(repoDto);

        assertNotNull(result);
        assertEquals(repoDto.id(), result.id());
        assertEquals(repoDto.userid(), result.userid());
        verify(todoRepository, times(1)).save(any(TodoEntity.class));
    }

    @Test
    void findById_WhenExists_ShouldReturnDto() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todoEntity));

        Optional<TodoRepositoryDto> result = todoAdapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(repoDto.id(), result.get().id());
        verify(todoRepository, times(1)).findById(1L);
    }

    @Test
    void findByUserid_ShouldReturnListOfDtos() {
        when(todoRepository.findByUserid(100L)).thenReturn(List.of(todoEntity));

        List<TodoRepositoryDto> result = todoAdapter.findByUserid(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().userid());
        verify(todoRepository, times(1)).findByUserid(100L);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        doNothing().when(todoRepository).deleteById(1L);

        todoAdapter.deleteById(1L);

        verify(todoRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteByUserid_ShouldCallRepositoryDelete() {
        doNothing().when(todoRepository).deleteByUserid(100L);

        todoAdapter.deleteByUserid(100L);

        verify(todoRepository, times(1)).deleteByUserid(100L);
    }
}
