package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.mapper.TrainingMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.Training;
import com.rainett.model.TrainingType;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.repository.TrainingRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private TrainingRepository trainingRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private CreateTrainingRequest request;
    private Training training;
    private Trainee trainee;
    private Trainer trainer;
    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        request = new CreateTrainingRequest();
        training = new Training();
        trainee = new Trainee();
        trainee.setId(1L);
        trainer = new Trainer();
        trainer.setId(2L);
        specialization = new TrainingType();
        trainer.setSpecialization(specialization);
    }

    @Test
    @DisplayName("Creates training")
    void createTraining() {
        when(trainingMapper.toEntity(request)).thenReturn(training);
        when(traineeRepository.findByUsername(request.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername(request.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));

        trainingService.createTraining(request);

        verify(trainingRepository, times(1)).save(training);
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
        assertEquals(specialization, training.getTrainingType());
    }
    
    @Test
    @DisplayName("Throws exception when trainee not found")
    void createTraining_traineeNotFound() {
        when(trainingMapper.toEntity(request)).thenReturn(training);
        when(traineeRepository.findByUsername(request.getTraineeUsername()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> trainingService.createTraining(request));
    }

    @Test
    @DisplayName("Throws exception when trainer not found")
    void createTraining_trainerNotFound() {
        when(trainingMapper.toEntity(request)).thenReturn(training);
        when(traineeRepository.findByUsername(request.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUsername(request.getTrainerUsername()))
                .thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> trainingService.createTraining(request));
    }
}
