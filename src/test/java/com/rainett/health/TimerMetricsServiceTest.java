package com.rainett.health;

import static org.assertj.core.api.Assertions.assertThat;

import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimerMetricsServiceTest {
    @Test
    @DisplayName("Simulates processing")
    void shouldRecordTimeForProcessRequest() {
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();
        TimerMetricsService service = new TimerMetricsService(meterRegistry);

        service.processRequest();

        Timer timer = meterRegistry.find("custom.request.timer").timer();
        assertThat(timer).isNotNull();
        assertThat(timer.count()).isEqualTo(1);
        assertThat(timer.totalTime(TimeUnit.MILLISECONDS)).isGreaterThanOrEqualTo(500);
    }
}