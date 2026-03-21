package com.example.library.integration.controller;

import com.example.library.application.model.auth.AuthLoginRequest;
import com.example.library.testsupport.SqliteMvcIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqliteMvcIntegrationTest
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void generateTokenReturnsJwtForValidCredentials() throws Exception {
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("junior.dev")
                .password("Password123!")
                .build();

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.subject").value("junior.dev"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_API_USER"));
    }

    @Test
    void generateTokenReturnsUnauthorizedForInvalidCredentials() throws Exception {
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("junior.dev")
                .password("bad-password")
                .build();

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Usuario o contrasena incorrectos"))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.path").value("/auth/token"));
    }
}
