package com.rainett.service.impl;

import com.rainett.annotations.Authenticated;
import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.exceptions.EntityNotFoundException;
import com.rainett.mapper.TrainerMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.TrainingType;
import com.rainett.repository.TrainerRepository;
import com.rainett.repository.TrainingTypeRepository;
import com.rainett.service.TrainerService;
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
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final TrainingTypeRepository trainingTypeRepository;
    private final UserService userService;

    @Override
    @Transactional
    public Trainer createProfile(@Valid CreateTrainerRequest request) {
        log.info("Creating trainer profile for request {}", request);
        Trainer trainer = trainerMapper.toEntity(request);
        TrainingType trainingType = getTrainingType(request.getSpecialization());
        trainer.setSpecialization(trainingType);
        String username = userService
                .generateUsername(request.getFirstName(), request.getLastName());
        String password = userService.generatePassword();
        trainer.setUsername(username);
        trainer.setPassword(password);
        return trainerRepository.save(trainer);
    }

    @Override
    @Authenticated
    @Transactional(readOnly = true)
    public Trainer findByUsername(@Valid UsernameRequest request) {
        log.info("Finding trainer profile for request {}", request);
        return getTrainer(request.getUsername());
    }

    @Override
    @Authenticated
    @Transactional
    public Trainer updatePassword(@Valid UpdatePasswordRequest request) {
        log.info("Updating trainer password for request {}", request);
        Trainer trainer = getTrainer(request.getUsername());
        trainer.setPassword(request.getNewPassword());
        return trainer;
    }

    @Override
    @Authenticated
    @Transactional
    public Trainer updateTrainer(@Valid UpdateTrainerRequest request) {
        log.info("Updating trainer profile for request {}", request);
        Trainer trainer = getTrainer(request.getUsername());
        trainerMapper.updateEntity(trainer, request);
        if (userService.usernameRequiresUpdate(trainer, request)) {
            String username = userService
                    .generateUsername(request.getFirstName(), request.getLastName());
            trainer.setUsername(username);
        }
        return trainer;
    }

    @Override
    @Authenticated
    @Transactional
    public Trainer setActiveStatus(@Valid UpdateUserActiveRequest request) {
        log.info("Updating trainer active status for request {}", request);
        Trainer trainer = getTrainer(request.getUsername());
        trainer.setIsActive(request.isActive());
        trainer.setActiveUpdatedAt(LocalDateTime.now());
        return trainer;
    }

    @Override
    @Authenticated
    @Transactional(readOnly = true)
    public List<Trainer> getTrainersWithoutTraineeByUsername(@Valid UsernameRequest request) {
        log.info("Finding trainers without trainee for request {}", request);
        return trainerRepository.findWithoutTraineeByUsername(request.getUsername());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Trainee> getTraineesByTrainerUsername(String trainerUsername) {
        log.info("Finding trainees by trainer username {}", trainerUsername);
        return getTrainer(trainerUsername).getTrainees().stream().toList();
    }

    private Trainer getTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainer not found for username = [" + username + "]"));
    }

    private TrainingType getTrainingType(String name) {
        return trainingTypeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Training type not found for name = [" + name + "]"
                ));
    }
}
