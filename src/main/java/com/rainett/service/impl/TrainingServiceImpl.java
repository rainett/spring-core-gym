package com.rainett.service.impl;

import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.mapper.TrainingMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.repository.TrainingRepository;
import com.rainett.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingRepository trainingRepository;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TrainingMapper trainingMapper;

    @Override
    @Transactional
    public void createTraining(CreateTrainingRequest request) {
        Training training = trainingMapper.toEntity(request);
        Trainee trainee = getTrainee(request.getTraineeUsername());
        Trainer trainer = getTrainer(request.getTrainerUsername());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainer.getSpecialization());
        trainingRepository.save(training);
    }

    private Trainee getTrainee(String traineeUsername) {
        return traineeRepository.findByUsername(traineeUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainee not found for username = [" + traineeUsername + "]"));
    }

    private Trainer getTrainer(String trainerUsername) {
        return trainerRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trainer not found for username = [" + trainerUsername + "]"));
    }
}
