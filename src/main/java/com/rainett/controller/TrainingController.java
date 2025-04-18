package com.rainett.controller;

import com.rainett.logging.Loggable;
import com.rainett.annotations.openapi.NotFoundResponse;
import com.rainett.annotations.openapi.OkResponse;
import com.rainett.annotations.openapi.SecuredOperation;
import com.rainett.annotations.openapi.ValidationResponse;
import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trainings API", description = "Endpoints for managing trainings")
@Loggable
@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingService trainingService;

    @ValidationResponse
    @SecuredOperation
    @NotFoundResponse(description = "Trainer or trainee not found")
    @OkResponse(description = "Training created successfully")
    @Operation(
            summary = "Create training",
            description = "Creates a new training"
    )
    @PostMapping
    public ResponseEntity<Void> createTraining(@Valid @RequestBody CreateTrainingRequest request) {
        trainingService.createTraining(request);
        return ResponseEntity.ok().build();
    }
}
