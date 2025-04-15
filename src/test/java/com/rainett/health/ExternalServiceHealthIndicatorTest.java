package com.rainett.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Status;

class ExternalServiceHealthIndicatorTest {
    private final ExternalServiceHealthIndicator indicator = new ExternalServiceHealthIndicator();

    @Test
    @DisplayName("Returns false when service is not reachable")
    void testUnreachable() throws NoSuchFieldException, IllegalAccessException {
        Field field = indicator.getClass().getDeclaredField("host");
        field.setAccessible(true);
        field.set(indicator, "https://www.alksjdhlkjasdaklsh.com/");

        assertEquals(Status.DOWN, indicator.health().getStatus());
    }

    @Test
    @DisplayName("Returns true when service is reachable")
    void testReachable() throws NoSuchFieldException, IllegalAccessException {
        Field field = indicator.getClass().getDeclaredField("host");
        field.setAccessible(true);
        field.set(indicator, "https://www.google.com/");

        assertEquals(Status.UP, indicator.health().getStatus());
    }
}