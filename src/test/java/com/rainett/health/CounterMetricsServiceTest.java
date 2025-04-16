package com.rainett.health;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CounterMetricsServiceTest {
    @Test
    @DisplayName("Increments the counter")
    void testIncrement() {
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        CounterMetricsService service = new CounterMetricsService(registry);
        service.init();
        double value = service.recordEvent();
        assertEquals(1, value);
    }
}