package com.rainett.health;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CounterMetricsService {
    private final MeterRegistry meterRegistry;
    private Counter counter;

    @PostConstruct
    public void init() {
        counter = meterRegistry.counter("custom.counter", "type", "event");
    }

    public double recordEvent() {
        counter.increment();
        return counter.count();
    }
}
