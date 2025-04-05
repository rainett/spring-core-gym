package com.rainett.service.impl;

import com.rainett.annotations.Authenticated;
import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.dto.user.UsernameRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.exceptions.EntityNotFoundException;
import com.rainett.mapper.TraineeMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.service.TraineeService;
import com.rainett.service.UserService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper traineeMapper;
    private final UserService userService;

    @Override
    @Transactional
    public Trainee createProfile(@Valid CreateTraineeRequest request) {
        log.info("Creating trainee profile for request {}", request);
        Trainee trainee = traineeMapper.toEntity(request);
        trainee.setIsActive(true);
        String username = userService
                .generateUsername(request.getFirstName(), request.getLastName());
        String password = userService.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        return traineeRepository.save(trainee);
    }

    @Override
    @Authenticated
    @Transactional(readOnly = true)
    public Trainee findByUsername(@Valid UsernameRequest request) {
        log.info("Finding trainee profile for request {}", request);
        return getTrainee(request.getUsername());
    }

    @Override
    @Authenticated
    @Transactional
    public Trainee updatePassword(@Valid UpdatePasswordRequest request) {
        log.info("Updating trainee password for request {}", request);
        Trainee trainee = getTrainee(request.getUsername());
        trainee.setPassword(request.getNewPassword());
        return trainee;
    }

    @Override
    @Authenticated
    @Transactional
    public Trainee updateTrainee(@Valid UpdateTraineeRequest request) {
        log.info("Updating trainee profile for request {}", request);
        Trainee trainee = getTrainee(request.getUsername());
        if (userService.usernameRequiresUpdate(trainee, request)) {
            traineeMapper.updateEntity(trainee, request);
            String username = userService
                    .generateUsername(request.getFirstName(), request.getLastName());
            trainee.setUsername(username);
        }
        return trainee;
    }

    @Override
    @Authenticated
    @Transactional
    public Trainee setActiveStatus(@Valid UpdateUserActiveRequest request) {
        log.info("Updating trainee active status for request {}", request);
        Trainee trainee = getTrainee(request.getUsername());
        trainee.setIsActive(request.isActive());
        trainee.setActiveUpdatedAt(LocalDateTime.now());
        return trainee;
    }

    @Override
    @Authenticated
    @Transactional
    public void deleteProfile(@Valid UsernameRequest request) {
        log.info("Deleting trainee profile for request {}", request);
        Trainee trainee = getTrainee(request.getUsername());
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee);
        }
        traineeRepository.delete(trainee);
    }

    @Override
    @Authenticated
    @Transactional
    public Trainee updateTrainers(String username, @Valid UpdateTraineeTrainersRequest request) {
        log.info("Updating trainee trainers for request {}", request);
        Trainee trainee = getTrainee(request.getUsername());
        List<Trainer> trainers = trainerRepository.findByUsernames(request.getTrainersUsernames());
        trainee.updateTrainers(trainers);
        return trainee;
    }

    private Trainee getTrainee(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainee not found for username = [" + username + "]"));
    }
}
