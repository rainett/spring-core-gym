package com.rainett.service.impl;

import com.rainett.annotations.Authenticated;
import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.dto.training.FindTraineeTrainingsRequest;
import com.rainett.dto.training.FindTrainerTrainingsRequest;
import com.rainett.exceptions.EntityNotFoundException;
import com.rainett.mapper.TrainingMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.model.TrainingType;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.repository.TrainingRepository;
import com.rainett.repository.TrainingTypeRepository;
import com.rainett.service.TrainingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;

    @Override
    @Authenticated
    @Transactional
    public Training createTraining(@Valid CreateTrainingRequest request) {
        log.info("Creating training for request {}", request);
        Training training = trainingMapper.toEntity(request);
        TrainingType trainingType = getTrainingType(request.getTrainingType());
        training.setTrainingType(trainingType);
        Trainee trainee = getTrainee(request.getTraineeUsername());
        Trainer trainer = getTrainer(request.getTrainerUsername());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        return trainingRepository.save(training);
    }

    @Override
    @Authenticated
    @Transactional(readOnly = true)
    public List<Training> findTrainingsForTrainee(@Valid FindTraineeTrainingsRequest request) {
        log.info("Finding trainings for trainee for request {}", request);
        return trainingRepository.findTraineeTrainings(request);
    }

    @Override
    @Authenticated
    @Transactional(readOnly = true)
    public List<Training> findTrainingsForTrainer(@Valid FindTrainerTrainingsRequest request) {
        log.info("Finding trainings for trainer for request {}", request);
        return trainingRepository.findTrainerTrainings(request);
    }

    private Trainee getTrainee(String traineeUsername) {
        return traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainee not found for username = [" + traineeUsername + "]"));
    }

    private Trainer getTrainer(String trainerUsername) {
        return trainerRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainer not found for username = [" + trainerUsername + "]"));
    }

    private TrainingType getTrainingType(String name) {
        return trainingTypeRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Training type not found for name = [" + name + "]"
                ));
    }
}
