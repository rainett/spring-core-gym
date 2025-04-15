package com.rainett.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class GaugeMetricsServiceTest {
    private GaugeMetricsService service;

    @BeforeEach
    void setUp() {
        service = new GaugeMetricsService(new SimpleMeterRegistry());
    }

    @Test
    @DisplayName("Increments")
    @Order(1)
    void testIncrement() {
        int value = service.incrementActiveUsers();
        assertEquals(1, value);
    }

    @Test
    @DisplayName("Decrements")
    @Order(2)
    void testDecrement() {
        int value = service.decrementActiveUsers();
        assertEquals(-1, value);
    }
}
