package com.rainett.health;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class TimerMetricsService {
    private final Timer timer;

    public TimerMetricsService(MeterRegistry meterRegistry) {
        this.timer = Timer.builder("custom.request.timer")
                .description("Time taken to process a custom request")
                .register(meterRegistry);
    }

    public void processRequest() {
        timer.record(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
