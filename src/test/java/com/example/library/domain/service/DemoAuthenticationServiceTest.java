package com.example.library.domain.service;

import com.example.library.application.model.auth.AuthLoginRequest;
import com.example.library.application.model.auth.AuthTokenResponse;
import com.example.library.application.model.exceptions.InvalidCredentialsException;
import com.example.library.domain.model.DemoAuthUser;
import com.example.library.infrastructure.persistence.repository.DemoAuthUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DemoAuthenticationServiceTest {

    @Mock
    private DemoAuthUserRepository demoAuthUserRepository;

    @Mock
    private JwtSimpleService jwtSimpleService;

    @InjectMocks
    private DemoAuthenticationService demoAuthenticationService;

    @Test
    void authenticateReturnsTokenWhenCredentialsAreValid() {
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("junior.dev")
                .password("Password123!")
                .build();
        DemoAuthUser user = DemoAuthUser.builder()
                .username("junior.dev")
                .password("Password123!")
                .roles(List.of("ROLE_API_USER"))
                .build();
        AuthTokenResponse tokenResponse = AuthTokenResponse.builder()
                .tokenType("Bearer")
                .accessToken("token")
                .expiresAt(Instant.now())
                .expiresInSeconds(1L).build();

        given(demoAuthUserRepository.findByUsername("junior.dev")).willReturn(Optional.of(user));
        given(jwtSimpleService.generateToken(user)).willReturn(tokenResponse);

        AuthTokenResponse result = demoAuthenticationService.authenticate(request);

        assertThat(result).isEqualTo(tokenResponse);
        verify(demoAuthUserRepository).findByUsername("junior.dev");
        verify(jwtSimpleService).generateToken(user);
    }

    @Test
    void authenticateRejectsNullRequest() {
        assertThatThrownBy(() -> demoAuthenticationService.authenticate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Debes informar username y password");
    }

    @Test
    void authenticateRejectsBlankCredentials() {
        AuthLoginRequest request = AuthLoginRequest.builder().username(" ").password("").build();

        assertThatThrownBy(() -> demoAuthenticationService.authenticate(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Debes informar username y password");
    }

    @Test
    void authenticateRejectsUnknownUser() {
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("missing")
                .password("Password123!")
                .build();

        given(demoAuthUserRepository.findByUsername("missing")).willReturn(Optional.empty());

        assertThatThrownBy(() -> demoAuthenticationService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Usuario o contrasena incorrectos");
    }

    @Test
    void authenticateRejectsWrongPassword() {
        AuthLoginRequest request = AuthLoginRequest.builder()
                .username("junior.dev")
                .password("wrong")
                .build();
        DemoAuthUser user = DemoAuthUser.builder()
                .username("junior.dev")
                .password("Password123!")
                .build();

        given(demoAuthUserRepository.findByUsername("junior.dev")).willReturn(Optional.of(user));

        assertThatThrownBy(() -> demoAuthenticationService.authenticate(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Usuario o contrasena incorrectos");
    }
}
