package com.example.library.domain.service;

import com.example.library.application.model.auth.AuthTokenResponse;
import com.example.library.domain.model.DemoAuthUser;
import com.example.library.domain.model.JwtSimpleProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtSimpleServiceTest {

    @Test
    void validateConfigurationRejectsShortSecret() {
        JwtSimpleProperties properties = new JwtSimpleProperties();
        properties.setIssuer("library");
        properties.setExpirationMinutes(30);
        properties.setSecret("short-secret");

        JwtSimpleService service = new JwtSimpleService(properties);

        assertThatThrownBy(service::validateConfiguration)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("security.jwt-simple.secret debe tener al menos 32 bytes");
    }

    @Test
    void generateTokenUsesDefaultsAndCanBeValidated() {
        JwtSimpleProperties properties = new JwtSimpleProperties();
        properties.setIssuer("library-api");
        properties.setExpirationMinutes(30);
        properties.setSecret("12345678901234567890123456789012");
        JwtSimpleService service = new JwtSimpleService(properties);
        DemoAuthUser user = DemoAuthUser.builder()
                .username("")
                .fullName(null)
                .email("junior.dev@library.local")
                .roles(List.of())
                .build();

        service.validateConfiguration();
        AuthTokenResponse token = service.generateToken(user);
        Claims claims = service.validateToken(token.getAccessToken());
        Collection<? extends GrantedAuthority> authorities = service.extractAuthorities(claims);

        assertThat(token.getTokenType()).isEqualTo("Bearer");
        assertThat(token.getSubject()).isEqualTo("demo-user");
        assertThat(token.getRoles()).containsExactly("ROLE_API_USER");
        assertThat(token.getExpiresInSeconds()).isEqualTo(1800);
        assertThat(claims.getIssuer()).isEqualTo("library-api");
        assertThat(claims.getSubject()).isEqualTo("demo-user");
        assertThat(claims.get("name", String.class)).isEqualTo("demo-user");
        assertThat(claims.get("email", String.class)).isEqualTo("junior.dev@library.local");
        assertThat(authorities).extracting(GrantedAuthority::getAuthority).containsExactly("ROLE_API_USER");
    }

    @Test
    void validateTokenRejectsInvalidToken() {
        JwtSimpleProperties properties = new JwtSimpleProperties();
        properties.setIssuer("library-api");
        properties.setExpirationMinutes(30);
        properties.setSecret("12345678901234567890123456789012");
        JwtSimpleService service = new JwtSimpleService(properties);

        assertThatThrownBy(() -> service.validateToken("not-a-jwt"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void extractAuthoritiesReturnsEmptyCollectionWhenRolesClaimIsNotACollection() {
        JwtSimpleProperties properties = new JwtSimpleProperties();
        properties.setIssuer("library-api");
        properties.setExpirationMinutes(30);
        properties.setSecret("12345678901234567890123456789012");
        JwtSimpleService service = new JwtSimpleService(properties);
        Claims claims = Mockito.mock(Claims.class);

        Mockito.when(claims.get("roles")).thenReturn("ROLE_ADMIN");

        assertThat(service.extractAuthorities(claims)).isEmpty();
    }
}
