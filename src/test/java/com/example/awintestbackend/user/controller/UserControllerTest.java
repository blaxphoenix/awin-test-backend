package com.example.awintestbackend.user.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.user.service.UserService;
import com.example.awintestbackend.user.service.UserServiceDto;
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

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserControllerDto inputDto = new UserControllerDto(null, "John Doe", "john.doe@example.com");
        UserServiceDto createdServiceDto = new UserServiceDto(1L, "John Doe", "john.doe@example.com");

        when(userService.createUser(any(UserServiceDto.class))).thenReturn(createdServiceDto);

        mockMvc.perform(post("/u2m/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userid").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(userService, times(1)).createUser(any(UserServiceDto.class));
    }

    @Test
    void createUser_WithInvalidInput_ShouldReturnBadRequest() throws Exception {
        UserControllerDto invalidDto = new UserControllerDto(null, "", "invalid-email");

        mockMvc.perform(post("/u2m/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        UserServiceDto userDto = new UserServiceDto(1L, "John Doe", "john.doe@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/u2m/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/u2m/v1/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        UserServiceDto userDto = new UserServiceDto(1L, "John Doe", "john.doe@example.com");
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/u2m/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userid").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserControllerDto inputDto = new UserControllerDto(1L, "John Updated", "john.updated@example.com");
        UserServiceDto updatedServiceDto = new UserServiceDto(1L, "John Updated", "john.updated@example.com");

        when(userService.updateUser(eq(1L), any(UserServiceDto.class))).thenReturn(updatedServiceDto);

        mockMvc.perform(put("/u2m/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(1L))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/u2m/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
