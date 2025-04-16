package com.rainett.health;

import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

@Component
public class GaugeMetricsService {
    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeUsers = new AtomicInteger(0);

    public GaugeMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        registerGauge();
    }

    public void registerGauge() {
        meterRegistry.gauge("custom.active.users", activeUsers);
    }

    public int incrementActiveUsers() {
        return activeUsers.incrementAndGet();
    }

    public int decrementActiveUsers() {
        return activeUsers.decrementAndGet();
    }
}
