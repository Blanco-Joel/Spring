package com.example.library.domain.service;

import com.example.library.application.model.auth.AuthLoginRequest;
import com.example.library.application.model.auth.AuthTokenResponse;
import com.example.library.application.model.exceptions.InvalidCredentialsException;
import com.example.library.domain.model.DemoAuthUser;
import com.example.library.infrastructure.persistence.repository.DemoAuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DemoAuthenticationService {

    private final DemoAuthUserRepository demoAuthUserRepository;
    private final JwtSimpleService jwtSimpleService;

    public AuthTokenResponse authenticate(AuthLoginRequest request) {
        validateRequest(request);

        DemoAuthUser user = demoAuthUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Usuario o contrasena incorrectos"));

        if (!Objects.equals(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuario o contrasena incorrectos");
        }

        return jwtSimpleService.generateToken(user);
    }

    private void validateRequest(AuthLoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Debes informar username y password");
        }
        if (!StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
            throw new IllegalArgumentException("Debes informar username y password");
        }
    }
}
