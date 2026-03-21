package com.example.library.application.controller;

import com.example.library.application.model.auth.AuthLoginRequest;
import com.example.library.application.model.auth.AuthTokenResponse;
import com.example.library.domain.service.DemoAuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private DemoAuthenticationService demoAuthenticationService;

    @InjectMocks
    private AuthController authController;

    @Test
    void generateTokenDelegatesToAuthenticationService() {
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("junior.dev")
                .password("Password123!")
                .build();
        AuthTokenResponse response = AuthTokenResponse.builder()
                .tokenType("Bearer")
                .accessToken("jwt-token")
                .expiresAt(Instant.now())
                .expiresInSeconds(1L).build();

        given(demoAuthenticationService.authenticate(request)).willReturn(response);

        ResponseEntity<AuthTokenResponse> result = authController.generateToken(request);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(response);
    }
}
