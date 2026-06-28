package com.example.awintestbackend.config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenNoCredentials_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/u2m/v1/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenCorrectCredentials_thenAuthorized() throws Exception {
        mockMvc.perform(get("/u2m/v1/users")
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isOk());
    }

    @Test
    void whenWrongCredentials_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/u2m/v1/users")
                        .with(httpBasic("wrong", "user")))
                .andExpect(status().isUnauthorized());
    }
}
