package com.rainett.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.security.JwtFilter;
import com.rainett.service.TraineeService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = TraineeController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtFilter.class))
class TraineeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TraineeService traineeService;

    @Test
    @DisplayName("GET /api/trainees/{username} returns trainee")
    void getTraineeByUsername() throws Exception {
        TraineeResponse response = new TraineeResponse("john.doe", "John", "Doe", "2000-01-01",
                "123 Main St", true, List.of());
        Mockito.when(traineeService.findByUsername("john.doe")).thenReturn(response);

        mockMvc.perform(get("/api/trainees/john.doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @DisplayName("GET /api/trainees/{username}/trainings returns trainings")
    void getTraineeTrainings() throws Exception {
        List<TraineeTrainingsResponse> trainings =
                List.of(new TraineeTrainingsResponse());
        Mockito.when(traineeService.findTrainings(anyString(), any(), any(), any(), any()))
                .thenReturn(trainings);

        mockMvc.perform(get("/api/trainees/john.doe/trainings")
                        .param("from", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/trainees/{username}/unassigned-trainers returns trainers")
    void getUnassignedTrainers() throws Exception {
        List<TrainerDto> trainers = List.of(new TrainerDto());
        Mockito.when(traineeService.findUnassignedTrainers("john.doe")).thenReturn(trainers);

        mockMvc.perform(get("/api/trainees/john.doe/unassigned-trainers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("POST /api/trainees creates trainee")
    void createTrainee() throws Exception {
        CreateTraineeRequest request =
                new CreateTraineeRequest("John", "Doe", LocalDate.now(), "123 Main St");
        UserCredentialsResponse response =
                new UserCredentialsResponse("john.doe", "generatedPass", "token");
        Mockito.when(traineeService.createProfile(any())).thenReturn(response);

        mockMvc.perform(post("/api/trainees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @DisplayName("PUT /api/trainees/{username} updates trainee")
    void updateTrainee() throws Exception {
        UpdateTraineeRequest request =
                new UpdateTraineeRequest("John", "Doe", true, LocalDate.now(), "123 Main St");
        TraineeResponse response =
                new TraineeResponse("john", "John", "Doe", "2000-01-01", "123 Main St", true,
                        List.of());
        Mockito.when(traineeService.updateTrainee(eq("john"), any())).thenReturn(response);

        mockMvc.perform(put("/api/trainees/john")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("PUT /api/trainees/{username}/trainers updates trainers")
    void updateTrainers() throws Exception {
        UpdateTraineeTrainersRequest request =
                new UpdateTraineeTrainersRequest(List.of("trainer1", "trainer2"));
        List<TrainerDto> response = List.of(new TrainerDto(), new TrainerDto());
        Mockito.when(traineeService.updateTrainers(eq("john"), any())).thenReturn(response);

        mockMvc.perform(put("/api/trainees/john/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("DELETE /api/trainees/{username} deletes trainee")
    void deleteProfile() throws Exception {
        mockMvc.perform(delete("/api/trainees/john"))
                .andExpect(status().isNoContent());

        Mockito.verify(traineeService).deleteProfile("john");
    }
}