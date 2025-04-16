package com.rainett.service.impl;

import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.mapper.TrainerMapper;
import com.rainett.model.Trainer;
import com.rainett.model.TrainingType;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.repository.TrainingTypeRepository;
import com.rainett.service.CredentialService;
import com.rainett.service.TrainerService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private static final String NO_TRAINER_MESSAGE = "Trainer not found for username = [%s]";

    private final TrainerRepository trainerRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainerMapper trainerMapper;
    private final CredentialService credentialService;

    @Override
    @Transactional(readOnly = true)
    public TrainerResponse findByUsername(String username) {
        TrainerResponse trainerResponse = trainerRepository.findTrainerDtoByUsername(username)
                .orElseThrow(getNoTraineeExceptionSupplier(username));
        trainerResponse.setTrainees(traineeRepository.findTraineesDtoForTrainer(username));
        return trainerResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerTrainingResponse> findTrainings(String username, LocalDate from,
                                                       LocalDate to, String traineeUsername) {
        if (!trainerRepository.existsByUsername(username)) {
            throw getNoTraineeExceptionSupplier(username).get();
        }
        return trainerRepository.findTrainerTrainingsDto(username, from, to, traineeUsername);
    }

    @Override
    @Transactional
    public UserCredentialsResponse createProfile(CreateTrainerRequest request) {
        Trainer trainer = trainerMapper.toEntity(request);
        TrainingType trainingType = getTrainingType(request.getSpecialization());
        trainer.setSpecialization(trainingType);
        credentialService.createCredentials(trainer);
        trainer = trainerRepository.save(trainer);
        return new UserCredentialsResponse(trainer.getUsername(), trainer.getPassword());
    }

    @Override
    @Transactional
    public TrainerResponse updateTrainer(String username, UpdateTrainerRequest request) {
        Trainer trainer = getTrainer(username);
        TrainingType specialization = getTrainingType(request.getSpecialization());
        trainerMapper.updateEntity(trainer, request);
        trainer.setSpecialization(specialization);
        trainer.setActiveUpdatedAt(LocalDateTime.now());
        return trainerMapper.toDto(trainer);
    }

    private Trainer getTrainer(String username) {
        return trainerRepository.findByUsername(username)
                .orElseThrow(getNoTraineeExceptionSupplier(username));
    }

    private TrainingType getTrainingType(String name) {
        return trainingTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Training type not found for name = [" + name + "]"
                ));
    }

    private Supplier<ResourceNotFoundException> getNoTraineeExceptionSupplier(String username) {
        String message = String.format(NO_TRAINER_MESSAGE, username);
        return () -> new ResourceNotFoundException(message);
    }
}
