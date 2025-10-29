package com.microsoft.openai.samples.assistant.session;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Central catalogue for the personas that the sample banking assistant understands.
 * Keeping this information in a single place helps the frontend, backend and
 * business APIs stay in sync when the demo data changes.
 */
@Component
public class DemoUserRegistry {

    private final Map<String, DemoUser> usersByEmail;

    public DemoUserRegistry() {
        Map<String, DemoUser> seed = new LinkedHashMap<>();
        seed.put("carolina.lopez@coemlatam.com", new DemoUser(
                "carolina.lopez@coemlatam.com",
                "Carolina López",
                "1000",
                "MXN"));
        seed.put("diego.ramirez@coemlatam.com", new DemoUser(
                "diego.ramirez@coemlatam.com",
                "Diego Ramírez",
                "1010",
                "COP"));
        seed.put("luisa.martinez@coemlatam.com", new DemoUser(
                "luisa.martinez@coemlatam.com",
                "Luisa Martínez",
                "1020",
                "CLP"));

        this.usersByEmail = Collections.unmodifiableMap(seed);
    }

    public List<DemoUser> allUsers() {
        return List.copyOf(usersByEmail.values());
    }

    public Optional<DemoUser> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(usersByEmail.get(email.toLowerCase(java.util.Locale.ROOT)));
    }

    public DemoUser defaultUser() {
        return usersByEmail.values().iterator().next();
    }
}

