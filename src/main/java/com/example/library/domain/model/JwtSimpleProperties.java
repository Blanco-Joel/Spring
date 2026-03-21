package com.example.library.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt-simple")
public class JwtSimpleProperties {

    private String issuer;
    private String secret;
    private long expirationMinutes;
}
