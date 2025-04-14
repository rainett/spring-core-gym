package com.rainett.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalServiceHealthIndicator implements HealthIndicator {
    private static final String HOST = "https://www.strava.com/";

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
            ResponseEntity<String> response = restTemplate.getForEntity(HOST, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
