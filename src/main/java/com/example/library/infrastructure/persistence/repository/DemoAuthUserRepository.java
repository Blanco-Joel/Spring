package com.example.library.infrastructure.persistence.repository;

import com.example.library.domain.model.DemoAuthUser;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DemoAuthUserRepository {

    private final Map<String, DemoAuthUser> usersByUsername = new ConcurrentHashMap<>();

    @PostConstruct
    void loadFakeUsers() {
        save(DemoAuthUser.builder()
                .username("junior.dev")
                .fullName("Junior Developer")
                .email("junior.dev@library.local")
                .password("Password123!")
                .roles(List.of("ROLE_API_USER"))
                .build());

        save(DemoAuthUser.builder()
                .username("admin.demo")
                .fullName("Admin Demo")
                .email("admin.demo@library.local")
                .password("Admin123!")
                .roles(List.of("ROLE_API_USER", "ROLE_ADMIN"))
                .build());
    }

    public Optional<DemoAuthUser> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    private void save(DemoAuthUser user) {
        usersByUsername.put(user.getUsername(), user);
    }
}
