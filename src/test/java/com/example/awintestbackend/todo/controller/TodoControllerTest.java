package com.example.awintestbackend.todo.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.exception.GlobalExceptionHandler;
import com.example.awintestbackend.todo.TodoMapper;
import com.example.awintestbackend.todo.service.TodoData;
import com.example.awintestbackend.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@WithMockUser
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @MockitoBean
    private TodoMapper todoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTodo_ShouldReturnCreatedTodo() throws Exception {
        TodoControllerDto inputDto = new TodoControllerDto(null, 1L, "Test Todo", "icon", false);
        TodoData createdServiceDto = new TodoData(1L, 1L, "Test Todo", "icon", false);

        when(todoMapper.toData(any(TodoControllerDto.class))).thenReturn(createdServiceDto);
        when(todoService.createTodo(any(TodoData.class))).thenReturn(createdServiceDto);
        when(todoMapper.toControllerDto(any(TodoData.class))).thenReturn(new TodoControllerDto(1L, 1L, "Test Todo", "icon", false));

        mockMvc.perform(post("/u2m/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Todo"))
                .andExpect(jsonPath("$.userid").value(1L));

        verify(todoService, times(1)).createTodo(any(TodoData.class));
    }

    @Test
    void getTodoById_WhenExists_ShouldReturnTodo() throws Exception {
        TodoData todoDto = new TodoData(1L, 1L, "Test Todo", "icon", false);
        TodoControllerDto controllerDto = new TodoControllerDto(1L, 1L, "Test Todo", "icon", false);

        when(todoService.getTodoById(1L)).thenReturn(Optional.of(todoDto));
        when(todoMapper.toControllerDto(todoDto)).thenReturn(controllerDto);

        mockMvc.perform(get("/u2m/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Test Todo"));
    }

    @Test
    void getAllTodos_ShouldReturnList() throws Exception {
        TodoData todoDto = new TodoData(1L, 1L, "Test Todo", "icon", false);
        TodoControllerDto controllerDto = new TodoControllerDto(1L, 1L, "Test Todo", "icon", false);
        when(todoService.getAllTodos()).thenReturn(List.of(todoDto));
        when(todoMapper.toControllerDto(todoDto)).thenReturn(controllerDto);

        mockMvc.perform(get("/u2m/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getTodosByUserid_ShouldReturnFilteredList() throws Exception {
        TodoData todoDto = new TodoData(1L, 1L, "Test Todo", "icon", false);
        TodoControllerDto controllerDto = new TodoControllerDto(1L, 1L, "Test Todo", "icon", false);
        when(todoService.getTodosByUserid(1L)).thenReturn(List.of(todoDto));
        when(todoMapper.toControllerDto(todoDto)).thenReturn(controllerDto);

        mockMvc.perform(get("/u2m/v1/todos?userid=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userid").value(1L));
    }

    @Test
    void updateTodo_ShouldReturnUpdatedTodo() throws Exception {
        TodoControllerDto inputDto = new TodoControllerDto(1L, 1L, "Updated Todo", "icon", true);
        TodoData updatedServiceDto = new TodoData(1L, 1L, "Updated Todo", "icon", true);

        when(todoMapper.toData(any(TodoControllerDto.class))).thenReturn(updatedServiceDto);
        when(todoService.updateTodo(eq(1L), any(TodoData.class))).thenReturn(updatedServiceDto);
        when(todoMapper.toControllerDto(any(TodoData.class))).thenReturn(new TodoControllerDto(1L, 1L, "Updated Todo", "icon", true));

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
        TodoData toggledServiceDto = new TodoData(1L, 1L, "Test Todo", "icon", true);
        TodoControllerDto controllerDto = new TodoControllerDto(1L, 1L, "Test Todo", "icon", true);

        when(todoService.toggleTodoState(1L)).thenReturn(Optional.of(toggledServiceDto));
        when(todoMapper.toControllerDto(toggledServiceDto)).thenReturn(controllerDto);

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
                .andExpect(jsonPath("$.detail").value("Todo not found with id: 1"));
    }
}
