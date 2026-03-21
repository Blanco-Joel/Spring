package com.example.library.application.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {

    private String tokenType;
    private String accessToken;
    private String subject;
    private Instant expiresAt;
    private long expiresInSeconds;
    private List<String> roles;
}
