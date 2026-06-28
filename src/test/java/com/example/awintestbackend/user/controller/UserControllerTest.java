package com.example.awintestbackend.user.controller;

import com.example.awintestbackend.config.SecurityConfig;
import com.example.awintestbackend.exception.GlobalExceptionHandler;
import com.example.awintestbackend.user.service.UserData;
import com.example.awintestbackend.user.service.UserService;
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

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserControllerDto inputDto = new UserControllerDto(null, "John Doe", "john.doe@example.com", "EUR");
        UserData createdServiceDto = new UserData(1L, "John Doe", "john.doe@example.com", "EUR");

        when(userService.createUser(any(UserData.class))).thenReturn(createdServiceDto);

        mockMvc.perform(post("/u2m/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userid").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.currency").value("EUR"));

        verify(userService, times(1)).createUser(any(UserData.class));
    }

    @Test
    void createUser_WithInvalidInput_ShouldReturnBadRequestWithValidationErrors() throws Exception {
        UserControllerDto invalidDto = new UserControllerDto(null, "", "invalid-email", "US");

        mockMvc.perform(post("/u2m/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.detail").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").exists())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() throws Exception {
        UserData userDto = new UserData(1L, "John Doe", "john.doe@example.com", "EUR");

        when(userService.getUserById(1L)).thenReturn(Optional.of(userDto));

        mockMvc.perform(get("/u2m/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldReturnNotFoundWithBody() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/u2m/v1/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("User not found with id: 1"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() throws Exception {
        UserData userDto = new UserData(1L, "John Doe", "john.doe@example.com", "EUR");
        when(userService.getAllUsers()).thenReturn(List.of(userDto));

        mockMvc.perform(get("/u2m/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userid").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].currency").value("EUR"));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        UserControllerDto inputDto = new UserControllerDto(1L, "John Updated", "john.updated@example.com", "USD");
        UserData updatedServiceDto = new UserData(1L, "John Updated", "john.updated@example.com", "USD");

        when(userService.updateUser(eq(1L), any(UserData.class))).thenReturn(updatedServiceDto);

        mockMvc.perform(put("/u2m/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userid").value(1L))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/u2m/v1/users/1"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(1L);
    }
}
