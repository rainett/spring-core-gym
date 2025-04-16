package com.rainett.health;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.jdbc.core.JdbcTemplate;

@ExtendWith(MockitoExtension.class)
class DatabaseHealthIndicatorTest {
    @InjectMocks
    private DatabaseHealthIndicator databaseHealthIndicator;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Returns health-down when DB is not responding")
    void testDown() {
        doThrow(new RuntimeException()).when(jdbcTemplate).execute(anyString());
        Health health = databaseHealthIndicator.health();
        assertEquals(Status.DOWN, health.getStatus());
    }

    @Test
    @DisplayName("Returns health-up when DB is responding")
    void testUp() {
        doNothing().when(jdbcTemplate).execute(anyString());
        Health health = databaseHealthIndicator.health();
        assertEquals(Status.UP, health.getStatus());
    }
}