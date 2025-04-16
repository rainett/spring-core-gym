package com.rainett.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Health health() {
        boolean databaseUp = checkDatabaseHealth();
        if (databaseUp) {
            return Health.up().withDetail("Database", "Available").build();
        } else {
            return Health.down().withDetail("Database", "Not available").build();
        }
    }

    private boolean checkDatabaseHealth() {
        try {
            jdbcTemplate.execute("SELECT 1");
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
