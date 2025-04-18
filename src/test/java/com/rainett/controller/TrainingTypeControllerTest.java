package com.rainett.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.security.JwtFilter;
import com.rainett.service.TrainingTypeService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = TrainingTypeController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtFilter.class))
class TrainingTypeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainingTypeService trainingTypeService;

    @Test
    @DisplayName("GET /api/training-types returns training types")
    void getTrainingTypes() throws Exception {
        when(trainingTypeService.getTrainingTypes()).thenReturn(
                List.of(new TrainingTypeResponse(1L, "Yoga")));

        mockMvc.perform(get("/api/training-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}