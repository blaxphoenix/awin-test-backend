package com.example.awintestbackend.todo.service;

import com.example.awintestbackend.todo.repository.TodoAdapter;
import com.example.awintestbackend.todo.repository.TodoRepositoryDto;
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
class TodoServiceImplTest {

    @Mock
    private TodoAdapter todoAdapter;

    @InjectMocks
    private TodoServiceImpl todoService;

    private TodoData serviceDto;
    private TodoRepositoryDto repoDto;

    @BeforeEach
    void setUp() {
        serviceDto = new TodoData(1L, 100L, "Task 1", "icon-1", false);
        repoDto = new TodoRepositoryDto(1L, 100L, "Task 1", "icon-1", false);
    }

    @Test
    void createTodo_ShouldReturnCreatedTodo() {
        when(todoAdapter.save(any(TodoRepositoryDto.class))).thenReturn(repoDto);

        TodoData result = todoService.createTodo(serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.id(), result.id());
        assertEquals(serviceDto.userid(), result.userid());
        assertEquals(serviceDto.description(), result.description());
        assertEquals(serviceDto.icon(), result.icon());
        assertFalse(result.state());
        verify(todoAdapter, times(1)).save(any(TodoRepositoryDto.class));
    }

    @Test
    void getTodoById_WhenTodoExists_ShouldReturnTodo() {
        when(todoAdapter.findById(1L)).thenReturn(Optional.of(repoDto));

        Optional<TodoData> result = todoService.getTodoById(1L);

        assertTrue(result.isPresent());
        assertEquals(serviceDto.id(), result.get().id());
        verify(todoAdapter, times(1)).findById(1L);
    }

    @Test
    void getTodosByUserid_ShouldReturnListOfTodos() {
        when(todoAdapter.findByUserid(100L)).thenReturn(List.of(repoDto));

        List<TodoData> result = todoService.getTodosByUserid(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.getFirst().userid());
        verify(todoAdapter, times(1)).findByUserid(100L);
    }

    @Test
    void updateTodo_ShouldReturnUpdatedTodo() {
        when(todoAdapter.save(any(TodoRepositoryDto.class))).thenReturn(repoDto);

        TodoData result = todoService.updateTodo(1L, serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.id(), result.id());
        verify(todoAdapter, times(1)).save(any(TodoRepositoryDto.class));
    }

    @Test
    void deleteTodo_ShouldCallAdapterDelete() {
        doNothing().when(todoAdapter).deleteById(1L);

        todoService.deleteTodo(1L);

        verify(todoAdapter, times(1)).deleteById(1L);
    }

    @Test
    void toggleTodoState_ShouldToggleState() {
        TodoRepositoryDto initialRepoDto = new TodoRepositoryDto(1L, 100L, "Task 1", "icon-1", false);
        TodoRepositoryDto toggledRepoDto = new TodoRepositoryDto(1L, 100L, "Task 1", "icon-1", true);

        when(todoAdapter.findById(1L)).thenReturn(Optional.of(initialRepoDto));
        when(todoAdapter.save(any(TodoRepositoryDto.class))).thenReturn(toggledRepoDto);

        Optional<TodoData> result = todoService.toggleTodoState(1L);

        assertTrue(result.isPresent());
        assertTrue(result.get().state());
        verify(todoAdapter, times(1)).findById(1L);
        verify(todoAdapter, times(1)).save(argThat(TodoRepositoryDto::state));
    }
}
