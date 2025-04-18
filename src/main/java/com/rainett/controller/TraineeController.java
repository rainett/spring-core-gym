package com.rainett.controller;

import com.rainett.annotations.openapi.CreatedResponse;
import com.rainett.annotations.openapi.DeletedResponse;
import com.rainett.annotations.openapi.NotFoundResponse;
import com.rainett.annotations.openapi.OkResponse;
import com.rainett.annotations.openapi.SecuredOperation;
import com.rainett.annotations.openapi.ValidationResponse;
import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.logging.Loggable;
import com.rainett.service.TraineeService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Trainee API", description = "Endpoints for managing trainees")
@Loggable
@RestController
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    @SecuredOperation
    @NotFoundResponse(description = "Trainee not found")
    @OkResponse(description = "Trainee found successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TraineeResponse.class))
    )
    @Operation(
            summary = "Get trainee by username",
            description = "Retrieves a trainee by username"
    )
    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponse> getTraineeByUsername(
            @PathVariable("username") String username) {
        TraineeResponse traineeResponse = traineeService.findByUsername(username);
        return ResponseEntity.ok(traineeResponse);
    }

    @SecuredOperation
    @NotFoundResponse(description = "Trainee not found")
    @OkResponse(description = "Trainings found successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = TraineeTrainingsResponse.class)
                    )
            )
    )
    @Operation(
            summary = "Get trainee trainings",
            description = "Retrieves trainings for a trainee"
    )
    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingsResponse>> getTraineeTrainings(
            @PathVariable("username") String username,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String trainerUsername,
            @RequestParam(required = false) String trainingType) {
        List<TraineeTrainingsResponse> traineeTrainingsResponse = traineeService
                .findTrainings(username, from, to, trainerUsername, trainingType);
        return ResponseEntity.ok(traineeTrainingsResponse);
    }

    @SecuredOperation
    @NotFoundResponse(description = "Trainee not found")
    @OkResponse(description = "Trainers found successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = TrainerDto.class)
                    )
            )
    )
    @Operation(
            summary = "Get unassigned trainers",
            description = "Retrieves unassigned trainers for a trainee"
    )
    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TrainerDto>> getUnassignedTrainers(
            @PathVariable("username") String username) {
        List<TrainerDto> trainerDto = traineeService.findUnassignedTrainers(username);
        return ResponseEntity.ok(trainerDto);
    }

    @ValidationResponse
    @CreatedResponse(description = "Trainee created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserCredentialsResponse.class)))
    @Operation(
            summary = "Create trainee",
            description = "Creates a new trainee"
    )
    @PostMapping
    public ResponseEntity<UserCredentialsResponse> createTrainee(
            @Valid @RequestBody CreateTraineeRequest request) {
        UserCredentialsResponse userCredentialsResponse = traineeService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCredentialsResponse);
    }

    @ValidationResponse
    @SecuredOperation
    @NotFoundResponse(description = "Trainee not found")
    @OkResponse(description = "Trainee updated successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TraineeResponse.class)))
    @Operation(
            summary = "Update trainee",
            description = "Updates a trainee"
    )
    @PutMapping("/{username}")
    public ResponseEntity<TraineeResponse> updateTrainee(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdateTraineeRequest request) {
        TraineeResponse traineeResponse = traineeService.updateTrainee(username, request);
        return ResponseEntity.ok(traineeResponse);
    }

    @ValidationResponse
    @SecuredOperation
    @NotFoundResponse(description = "Trainee not found")
    @OkResponse(description = "Trainers updated successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = TrainerDto.class)
                    )
            )
    )
    @Operation(
            summary = "Update trainers",
            description = "Updates trainers for a trainee"
    )
    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TrainerDto>> updateTrainers(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdateTraineeTrainersRequest request) {
        List<TrainerDto> trainerDto = traineeService.updateTrainers(username, request);
        return ResponseEntity.ok(trainerDto);
    }

    @SecuredOperation
    @NotFoundResponse(description = "Trainee not found")
    @DeletedResponse(description = "Trainee deleted successfully")
    @Operation(
            summary = "Delete trainee",
            description = "Deletes a trainee"
    )
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable("username") String username) {
        traineeService.deleteProfile(username);
        return ResponseEntity.noContent().build();
    }
}
