package com.rainett.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TraineeDto;
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
import com.rainett.service.ProfileCreationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainingTypeRepository trainingTypeRepository;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private ProfileCreationService<Trainer> profileCreationService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private final String username = "trainer1";
    private final String specializationName = "Fitness";

    private TrainerResponse trainerResponse;
    private TrainerTrainingResponse trainerTrainingResponse;
    private Trainer trainer;
    private TrainingType trainingType;

    @BeforeEach
    void setUp() {
        trainerResponse = new TrainerResponse();
        trainerResponse.setUsername(username);
        trainerTrainingResponse = new TrainerTrainingResponse();
        trainerTrainingResponse.setTrainingName("Cardio Session");
        trainer = new Trainer();
        trainer.setUsername(username);
        trainer.setPassword("password");
        trainingType = new TrainingType();
    }

    @Test
    void findByUsername_shouldReturnTrainerResponse_whenFound() {
        when(trainerRepository.findTrainerDtoByUsername(username))
                .thenReturn(Optional.of(trainerResponse));
        List<TraineeDto> dummyTrainees = List.of(new TraineeDto(), new TraineeDto());
        when(traineeRepository.findTraineesDtoForTrainer(username))
                .thenReturn(dummyTrainees);

        TrainerResponse response = trainerService.findByUsername(username);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getTrainees()).isEqualTo(dummyTrainees);
        verify(trainerRepository, times(1)).findTrainerDtoByUsername(username);
        verify(traineeRepository, times(1)).findTraineesDtoForTrainer(username);
    }

    @Test
    void findByUsername_shouldThrowResourceNotFound_whenTrainerDtoNotFound() {
        when(trainerRepository.findTrainerDtoByUsername(username))
                .thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class,
                () -> trainerService.findByUsername(username));
        String message = String.format("Trainer not found for username = [%s]", username);
        assertThat(ex.getMessage()).isEqualTo(message);
    }

    @Test
    void findTrainings_shouldReturnTrainings_whenTrainerExists() {
        LocalDate from = LocalDate.now().minusDays(5);
        LocalDate to = LocalDate.now();
        String traineeUsername = "trainee1";

        when(trainerRepository.existsByUsername(username)).thenReturn(true);
        List<TrainerTrainingResponse> trainings = List.of(trainerTrainingResponse);
        when(trainerRepository.findTrainerTrainingsDto(username, from, to, traineeUsername))
                .thenReturn(trainings);

        List<TrainerTrainingResponse> result =
                trainerService.findTrainings(username, from, to, traineeUsername);
        assertThat(result).isEqualTo(trainings);
    }

    @Test
    void findTrainings_shouldThrowResourceNotFound_whenTrainerDoesNotExist() {
        when(trainerRepository.existsByUsername(username)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> trainerService.findTrainings(username, null, null, null));
    }

    @Test
    void createProfile_shouldCreateTrainerAndReturnUserCredentials() {
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setSpecialization(specializationName);
        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(trainingTypeRepository.findByName(specializationName)).thenReturn(Optional.of(
                trainingType));
        when(profileCreationService.createProfile(trainer, trainerRepository))
                .thenReturn(new UserCredentialsResponse(username, trainer.getPassword(), "token"));

        UserCredentialsResponse response = trainerService.createProfile(request);

        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getPassword()).isEqualTo(trainer.getPassword());
    }

    @Test
    void createProfile_shouldThrowResourceNotFound_whenTrainingTypeNotFound() {
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setSpecialization(specializationName);
        when(trainerMapper.toEntity(request)).thenReturn(trainer);
        when(trainingTypeRepository.findByName(specializationName)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class,
                () -> trainerService.createProfile(request));
        assertThat(ex.getMessage()).contains(
                "Training type not found for name = [" + specializationName + "]");
    }

    @Test
    void updateTrainer_shouldUpdateTrainerAndReturnResponse() {
        UpdateTrainerRequest request = new UpdateTrainerRequest();
        request.setSpecialization(specializationName);
        when(trainerRepository.findByUsername(username)).thenReturn(Optional.of(trainer));
        when(trainingTypeRepository.findByName(specializationName)).thenReturn(Optional.of(
                trainingType));
        doNothing().when(trainerMapper).updateEntity(trainer, request);
        TrainerResponse updatedResponse = new TrainerResponse();
        updatedResponse.setUsername(username);
        when(trainerMapper.toDto(trainer)).thenReturn(updatedResponse);

        TrainerResponse response = trainerService.updateTrainer(username, request);
        assertThat(trainer.getSpecialization()).isEqualTo(trainingType);
        assertThat(trainer.getActiveUpdatedAt()).isNotNull();
        assertThat(response).isEqualTo(updatedResponse);
    }
}
