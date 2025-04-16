package com.rainett.controller;

import com.rainett.health.CounterMetricsService;
import com.rainett.health.GaugeMetricsService;
import com.rainett.health.TimerMetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class CounterController {
    private final CounterMetricsService counterService;
    private final GaugeMetricsService gaugeService;
    private final TimerMetricsService timerService;

    @PostMapping("/counter")
    public ResponseEntity<Response> recordEvent() {
        double value = counterService.recordEvent();
        return ResponseEntity.ok(new Response(value));
    }

    @PostMapping("/increment-gauge")
    public ResponseEntity<Response> increment() {
        int i = gaugeService.incrementActiveUsers();
        return ResponseEntity.ok(new Response(i));
    }

    @PostMapping("/timer")
    public ResponseEntity<Void> timer() {
        timerService.processRequest();
        return ResponseEntity.ok().build();
    }

    public record Response(double value) {}
}
