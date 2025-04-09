package com.rainett.controller;

import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.service.TrainerService;
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

@RestController
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
public class TrainerController {
    private final TrainerService trainerService;

    @GetMapping("/{username}")
    public ResponseEntity<TrainerResponse> findByUsername(
            @PathVariable("username") String username) {
        TrainerResponse trainerResponse = trainerService.findByUsername(username);
        return ResponseEntity.ok(trainerResponse);
    }

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

    @PostMapping
    public ResponseEntity<UserCredentialsResponse> createTrainer(
            @Valid @RequestBody CreateTrainerRequest request) {
        UserCredentialsResponse userCredentialsResponse = trainerService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCredentialsResponse);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerResponse> updateTrainer(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdateTrainerRequest request) {
        TrainerResponse trainerResponse = trainerService.updateTrainer(username, request);
        return ResponseEntity.ok(trainerResponse);
    }
}
