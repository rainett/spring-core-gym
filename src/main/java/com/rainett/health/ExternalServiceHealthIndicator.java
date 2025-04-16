package com.rainett.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalServiceHealthIndicator implements HealthIndicator {
    @Value("${external.service:https://strava.com}")
    private String host;

    @Override
    public Health health() {
        boolean serviceUp = checkServiceHealth();
        if (serviceUp) {
            return Health.up().withDetail("Service", "Available").build();
        } else {
            return Health.down().withDetail("Service", "Not available").build();
        }
    }

    private boolean checkServiceHealth() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(host, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            }
        } catch (Exception ex) {
            log.warn("An exception occurred while reaching {}", host, ex);
        }
        return false;
    }
}
