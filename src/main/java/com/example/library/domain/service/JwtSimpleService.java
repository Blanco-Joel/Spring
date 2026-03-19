package com.example.library.domain.service;

import com.example.library.application.model.auth.AuthTokenResponse;
import com.example.library.domain.model.DemoAuthUser;
import com.example.library.domain.model.JwtSimpleProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtSimpleService {

    private final JwtSimpleProperties jwtSimpleProperties;

    @PostConstruct
    void validateConfiguration() {
        if (jwtSimpleProperties.getSecret() == null
                || jwtSimpleProperties.getSecret().getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("security.jwt-simple.secret debe tener al menos 32 bytes");
        }
    }

    public AuthTokenResponse generateToken(DemoAuthUser user) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(jwtSimpleProperties.getExpirationMinutes(), ChronoUnit.MINUTES);

        String subject = StringUtils.hasText(user.getUsername()) ? user.getUsername() : "demo-user";
        String name = StringUtils.hasText(user.getFullName()) ? user.getFullName() : subject;
        List<String> roles = user.getRoles() == null || user.getRoles().isEmpty()
                ? List.of("ROLE_API_USER")
                : user.getRoles();

        String token = Jwts.builder()
                .issuer(jwtSimpleProperties.getIssuer())
                .subject(subject)
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .claim("name", name)
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .claim("cognito:groups", roles)
                .signWith(getSigningKey())
                .compact();

        return AuthTokenResponse.builder()
                .tokenType("Bearer")
                .accessToken(token)
                .subject(subject)
                .expiresAt(expiresAt)
                .expiresInSeconds(jwtSimpleProperties.getExpirationMinutes() * 60)
                .roles(roles)
                .build();
    }

    public Claims validateToken(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) {
        Object rolesClaim = claims.get("roles");
        if (!(rolesClaim instanceof Collection<?> roles)) {
            return List.of();
        }

        return roles.stream()
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSimpleProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}
