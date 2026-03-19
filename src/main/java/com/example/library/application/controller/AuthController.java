package com.example.library.application.controller;

import com.example.library.application.model.auth.AuthLoginRequest;
import com.example.library.application.model.auth.AuthTokenResponse;
import com.example.library.domain.service.DemoAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final DemoAuthenticationService demoAuthenticationService;

    @PostMapping("/token")
    public ResponseEntity<AuthTokenResponse> generateToken(@RequestBody AuthLoginRequest request) {
        return ResponseEntity.ok(demoAuthenticationService.authenticate(request));
    }
}
