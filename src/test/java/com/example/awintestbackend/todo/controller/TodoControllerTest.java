package com.example.awintestbackend.todo.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.exception.GlobalExceptionHandler;
import com.example.awintestbackend.todo.service.TodoService;
import com.example.awintestbackend.todo.service.TodoServiceDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTodo_ShouldReturnCreatedTodo() throws Exception {
        TodoControllerDto inputDto = new TodoControllerDto(null, 1L, "Test Todo", "icon", false);
        TodoServiceDto createdServiceDto = new TodoServiceDto(1L, 1L, "Test Todo", "icon", false);

        when(todoService.createTodo(any(TodoServiceDto.class))).thenReturn(createdServiceDto);

        mockMvc.perform(post("/u2m/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Todo"))
                .andExpect(jsonPath("$.userid").value(1L));

        verify(todoService, times(1)).createTodo(any(TodoServiceDto.class));
    }

    @Test
    void getTodoById_WhenExists_ShouldReturnTodo() throws Exception {
        TodoServiceDto todoDto = new TodoServiceDto(1L, 1L, "Test Todo", "icon", false);

        when(todoService.getTodoById(1L)).thenReturn(Optional.of(todoDto));

        mockMvc.perform(get("/u2m/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Todo"));
    }

    @Test
    void getAllTodos_ShouldReturnList() throws Exception {
        TodoServiceDto todoDto = new TodoServiceDto(1L, 1L, "Test Todo", "icon", false);
        when(todoService.getAllTodos()).thenReturn(List.of(todoDto));

        mockMvc.perform(get("/u2m/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getTodosByUserid_ShouldReturnFilteredList() throws Exception {
        TodoServiceDto todoDto = new TodoServiceDto(1L, 1L, "Test Todo", "icon", false);
        when(todoService.getTodosByUserid(1L)).thenReturn(List.of(todoDto));

        mockMvc.perform(get("/u2m/v1/todos?userid=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userid").value(1L));
    }

    @Test
    void updateTodo_ShouldReturnUpdatedTodo() throws Exception {
        TodoControllerDto inputDto = new TodoControllerDto(1L, 1L, "Updated Todo", "icon", true);
        TodoServiceDto updatedServiceDto = new TodoServiceDto(1L, 1L, "Updated Todo", "icon", true);

        when(todoService.updateTodo(eq(1L), any(TodoServiceDto.class))).thenReturn(updatedServiceDto);

        mockMvc.perform(put("/u2m/v1/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Todo"))
                .andExpect(jsonPath("$.state").value(true));
    }

    @Test
    void deleteTodo_ShouldReturnNoContent() throws Exception {
        doNothing().when(todoService).deleteTodo(1L);

        mockMvc.perform(delete("/u2m/v1/todos/1"))
                .andExpect(status().isNoContent());

        verify(todoService, times(1)).deleteTodo(1L);
    }

    @Test
    void toggleTodoState_ShouldReturnToggledTodo() throws Exception {
        TodoServiceDto toggledServiceDto = new TodoServiceDto(1L, 1L, "Test Todo", "icon", true);

        when(todoService.toggleTodoState(1L)).thenReturn(Optional.of(toggledServiceDto));

        mockMvc.perform(patch("/u2m/v1/todos/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.state").value(true));

        verify(todoService, times(1)).toggleTodoState(1L);
    }

    @Test
    void toggleTodoState_WhenNotFound_ShouldReturnNotFoundWithBody() throws Exception {
        when(todoService.toggleTodoState(1L)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/u2m/v1/todos/1/toggle"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Todo not found with id: 1"));
    }
}
