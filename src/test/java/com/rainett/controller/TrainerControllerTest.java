package com.rainett.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.service.TrainerService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrainerService trainerService;

    @Test
    @DisplayName("GET /api/trainers/{username} returns trainer")
    void getTrainerByUsername() throws Exception {
        TrainerResponse trainerResponse =
                new TrainerResponse("john.doe", "John", "Doe", "Strength", true, null);
        when(trainerService.findByUsername("john.doe")).thenReturn(trainerResponse);
        mockMvc.perform(get("/api/trainers/john.doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.doe"));
    }

    @Test
    @DisplayName("GET /api/trainers/{username}/trainings returns trainings")
    void getTrainerTrainings() throws Exception {
        List<TrainerTrainingResponse> result = List.of(new TrainerTrainingResponse(),
                new TrainerTrainingResponse());
        when(trainerService.findTrainings(anyString(), any(), any(), any())).thenReturn(result);

        mockMvc.perform(get("/api/trainers/john.doe/trainings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("POST /api/trainers creates trainer")
    void createTrainer() throws Exception {
        CreateTrainerRequest request = new CreateTrainerRequest("John", "Doe", "Boxing");
        UserCredentialsResponse response = new UserCredentialsResponse("john.doe", "generatedPass");
        when(trainerService.createProfile(request)).thenReturn(response);

        mockMvc.perform(post("/api/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("john.doe"));

    }

    @Test
    @DisplayName("PUT /api/trainers/{username} updates trainer")
    void updateTrainer() throws Exception {
        UpdateTrainerRequest request = new UpdateTrainerRequest("John", "Doe", true, "Boxing");
        TrainerResponse trainerResponse =
                new TrainerResponse("john.doe", "John", "Doe", "Boxing", true, null);
        when(trainerService.updateTrainer(anyString(), any())).thenReturn(trainerResponse);

        mockMvc.perform(put("/api/trainers/john.doe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }
}
