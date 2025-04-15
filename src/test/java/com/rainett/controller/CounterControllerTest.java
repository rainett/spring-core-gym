package com.rainett.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.health.CounterMetricsService;
import com.rainett.health.GaugeMetricsService;
import com.rainett.health.TimerMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = CounterController.class)
class CounterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CounterMetricsService counterService;

    @MockitoBean
    private GaugeMetricsService gaugeService;

    @MockitoBean
    private TimerMetricsService timerService;

    @BeforeEach
    void setUp() {
        when(counterService.recordEvent()).thenReturn(1.0);
        when(gaugeService.incrementActiveUsers()).thenReturn(1);
        doNothing().when(timerService).processRequest();
    }

    @Test
    @DisplayName("/counter")
    void recordEvent() throws Exception {
        CounterController.Response response = new CounterController.Response(1);
        String expected = objectMapper.writeValueAsString(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/metrics/counter")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expected));
        verify(counterService, times(1)).recordEvent();
    }

    @Test
    @DisplayName("/increment-gauge")
    void increment() throws Exception {
        CounterController.Response response = new CounterController.Response(1);
        String expected = objectMapper.writeValueAsString(response);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/metrics/increment-gauge")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expected));
        verify(gaugeService, times(1)).incrementActiveUsers();
    }

    @Test
    @DisplayName("/timer")
    void timer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/metrics/timer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        verify(timerService, times(1)).processRequest();
    }
}