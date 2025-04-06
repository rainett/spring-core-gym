package com.rainett.controller;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.service.TraineeService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
public class TraineeController {
    private final TraineeService traineeService;

    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponse> getTraineeByUsername(@PathVariable String username) {
        TraineeResponse traineeResponse = traineeService.findByUsername(username);
        return ResponseEntity.ok(traineeResponse);
    }

    @GetMapping("/{username}/trainings")
    public ResponseEntity<List<TraineeTrainingsResponse>> getTraineeTrainings(
            @PathVariable String username,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String trainerUsername,
            @RequestParam(required = false) String trainingType) {
        List<TraineeTrainingsResponse> traineeTrainingsResponse = traineeService
                .findTrainings(username, from, to, trainerUsername, trainingType);
        return ResponseEntity.ok(traineeTrainingsResponse);
    }

    @GetMapping("/{username}/unassigned-trainers")
    public ResponseEntity<List<TraineeTrainerDto>> getUnassignedTrainers(@PathVariable String username) {
        List<TraineeTrainerDto> traineeTrainerDto = traineeService.findUnassignedTrainers(username);
        return ResponseEntity.ok(traineeTrainerDto);
    }

    @PostMapping
    public ResponseEntity<UserCredentialsResponse> createTrainee(@Valid CreateTraineeRequest request) {
        UserCredentialsResponse userCredentialsResponse = traineeService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCredentialsResponse);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeResponse> updateTrainee(
            @PathVariable String username,
            @Valid UpdateTraineeRequest request) {
        TraineeResponse traineeResponse = traineeService.updateTrainee(username, request);
        return ResponseEntity.ok(traineeResponse);
    }

    @PutMapping("/{username}/trainers")
    public ResponseEntity<List<TraineeTrainerDto>> updateTrainers(
            @PathVariable String username,
            @Valid UpdateTraineeTrainersRequest request) {
        List<TraineeTrainerDto> trainerDto = traineeService.updateTrainers(username, request);
        return ResponseEntity.ok(trainerDto);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String username) {
        traineeService.deleteProfile(username);
        return ResponseEntity.noContent().build();
    }
}
