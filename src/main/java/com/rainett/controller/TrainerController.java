package com.rainett.controller;

import com.rainett.annotations.Authenticated;
import com.rainett.annotations.Loggable;
import com.rainett.annotations.openapi.CreatedResponse;
import com.rainett.annotations.openapi.NotFoundResponse;
import com.rainett.annotations.openapi.OkResponse;
import com.rainett.annotations.openapi.SecuredOperation;
import com.rainett.annotations.openapi.ValidationResponse;
import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.service.TrainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trainer API", description = "Endpoints for managing trainers")
@Loggable
@Authenticated
@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @SecuredOperation
    @NotFoundResponse(description = "Trainer not found")
    @OkResponse(description = "Trainer found successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainerResponse.class))
    )
    @Operation(
            summary = "Get trainer by username",
            description = "Retrieves a trainer by username"
    )
    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponse> findByUsername(
            @PathVariable("username") String username) {
        TrainerResponse trainerResponse = trainerService.findByUsername(username);
        return ResponseEntity.ok(trainerResponse);
    }

    @SecuredOperation
    @NotFoundResponse(description = "Trainer not found")
    @OkResponse(description = "Trainings found successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = TrainerTrainingResponse.class)
                    )
            )
    )
    @Operation(
            summary = "Get trainer trainings",
            description = "Retrieves trainings for a trainer"
    )
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TrainerTrainingResponse>> getTrainerTrainings(
            @PathVariable("username") String username,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String traineeUsername) {
        List<TrainerTrainingResponse> trainerTrainingResponse = trainerService
                .findTrainings(username, from, to, traineeUsername);
        return ResponseEntity.ok(trainerTrainingResponse);
    }

    @ValidationResponse
    @NotFoundResponse(description = "Training type not found")
    @CreatedResponse(description = "Trainer created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserCredentialsResponse.class)))
    @Operation(
            summary = "Create trainer",
            description = "Creates a new trainer"
    )
    @PostMapping
    @Authenticated(ignore = true)
    public ResponseEntity<UserCredentialsResponse> createTrainer(
            @Valid @RequestBody CreateTrainerRequest request) {
        UserCredentialsResponse userCredentialsResponse = trainerService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCredentialsResponse);
    }

    @ValidationResponse
    @SecuredOperation
    @NotFoundResponse(description = "Trainer or training type not found")
    @OkResponse(description = "Trainer updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainerResponse.class))
    )
    @Operation(
            summary = "Update trainer",
            description = "Updates a trainer"
    )
    @PutMapping("/{username}")
    public ResponseEntity<TrainerResponse> updateTrainer(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdateTrainerRequest request) {
        TrainerResponse trainerResponse = trainerService.updateTrainer(username, request);
        return ResponseEntity.ok(trainerResponse);
    }
}
