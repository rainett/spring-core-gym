package com.rainett.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.mapper.TraineeMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.service.ProfileCreationService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private ProfileCreationService<Trainee> profileCreationService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private final String username = "john.doe";
    private TraineeResponse traineeResponse;
    private Trainee trainee;
    private TrainerDto trainerDto;
    private TraineeTrainingsResponse trainingResponse;

    @BeforeEach
    void setUp() {
        traineeResponse = new TraineeResponse();
        traineeResponse.setUsername(username);

        trainerDto = new TrainerDto();
        trainerDto.setUsername("trainer1");
        trainerDto.setFirstName("Trainer");
        trainerDto.setLastName("One");

        trainingResponse = new TraineeTrainingsResponse();
        trainingResponse.setTrainingName("Yoga");

        trainee = new Trainee();
        trainee.setUsername(username);
        trainee.setPassword("password");
    }

    @Test
    void findByUsername_shouldReturnTraineeResponse_whenTraineeFound() {
        when(traineeRepository.findTraineeDtoByUsername(username))
                .thenReturn(Optional.of(traineeResponse));
        when(trainerRepository.findTrainersDtoForTrainee(username))
                .thenReturn(List.of(trainerDto));

        TraineeResponse response = traineeService.findByUsername(username);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getTrainers()).containsExactly(trainerDto);
        verify(traineeRepository, times(1)).findTraineeDtoByUsername(username);
        verify(trainerRepository, times(1)).findTrainersDtoForTrainee(username);
    }

    @Test
    void findByUsername_shouldThrowResourceNotFound_whenTraineeNotFound() {
        when(traineeRepository.findTraineeDtoByUsername(username)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                        () -> traineeService.findByUsername(username));
        assertThat(ex.getMessage()).contains(
                String.format("Trainee not found for username = [%s]", username));
    }

    @Test
    void findTrainings_shouldReturnTrainings_whenTraineeExists() {
        LocalDate from = LocalDate.now().minusDays(10);
        LocalDate to = LocalDate.now();
        String trainerUsername = "trainer1";
        String trainingType = "Yoga";

        when(traineeRepository.existsByUsername(username)).thenReturn(true);
        when(traineeRepository
                .findTraineeTrainingsDto(username, from, to, trainerUsername, trainingType))
                .thenReturn(List.of(trainingResponse));

        List<TraineeTrainingsResponse> trainings = traineeService.findTrainings(
                username, from, to, trainerUsername, trainingType);

        assertThat(trainings).hasSize(1);
        assertThat(trainings.get(0).getTrainingName()).isEqualTo("Yoga");
    }

    @Test
    void findTrainings_shouldThrowResourceNotFound_whenTraineeDoesNotExist() {
        when(traineeRepository.existsByUsername(username)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> traineeService.findTrainings(username, null, null, null, null));
    }

    @Test
    void findUnassignedTrainers_shouldReturnTrainerDtos_whenTraineeExists() {
        when(traineeRepository.existsByUsername(username)).thenReturn(true);
        List<TrainerDto> list = List.of(trainerDto);
        when(traineeRepository.findUnassignedTrainersDto(username)).thenReturn(list);

        List<TrainerDto> dtos = traineeService.findUnassignedTrainers(username);

        assertThat(dtos).isEqualTo(list);
    }

    @Test
    void findUnassignedTrainers_shouldThrowResourceNotFound_whenTraineeDoesNotExist() {
        when(traineeRepository.existsByUsername(username)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> traineeService.findUnassignedTrainers(username));
    }

    @Test
    void createProfile_shouldCreateProfileAndReturnUserCredentials() {
        CreateTraineeRequest request = new CreateTraineeRequest();
        when(traineeMapper.toEntity(request)).thenReturn(trainee);
        when(profileCreationService.createProfile(trainee, traineeRepository))
                .thenReturn(new UserCredentialsResponse(username, "password", "token"));

        UserCredentialsResponse result = traineeService.createProfile(request);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getPassword()).isEqualTo("password");
    }

    @Test
    void updateTrainee_shouldUpdateTraineeAndReturnResponse() {
        UpdateTraineeRequest request = new UpdateTraineeRequest();
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        doAnswer(invocation -> null).when(traineeMapper).updateEntity(trainee, request);
        when(traineeMapper.toDto(trainee)).thenReturn(traineeResponse);

        TraineeResponse response = traineeService.updateTrainee(username, request);

        assertThat(trainee.getActiveUpdatedAt()).isNotNull();
        assertThat(response).isEqualTo(traineeResponse);
    }

    @Test
    void updateTrainers_shouldUpdateTrainersAndReturnTrainerDtos() {
        UpdateTraineeTrainersRequest request = new UpdateTraineeTrainersRequest();
        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        request.setTrainersUsernames(trainerUsernames);

        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        trainer1.setUsername("trainer1");
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        trainer2.setUsername("trainer2");
        List<Trainer> trainers = List.of(trainer1, trainer2);
        when(trainerRepository.findByUsernameIn(trainerUsernames)).thenReturn(trainers);
        when(traineeMapper.toTrainerDto(any(Trainer.class))).thenAnswer(invocation -> {
            Trainer trainer = invocation.getArgument(0);
            TrainerDto dto = new TrainerDto();
            dto.setUsername(trainer.getUsername());
            return dto;
        });

        List<TrainerDto> result = traineeService.updateTrainers(username, request);

        assertThat(trainee.getTrainers()).containsExactlyInAnyOrderElementsOf(trainers);
        assertThat(result).hasSize(2);
    }

    @Test
    void deleteProfile_shouldRemoveTraineeFromAllTrainersAndDeleteTrainee()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Trainer trainer1 = new Trainer();
        trainer1.setId(1L);
        Trainer trainer2 = new Trainer();
        trainer2.setId(2L);
        Set<Trainer> trainersSet = new HashSet<>(Arrays.asList(trainer1, trainer2));

        Method setTrainers = trainee.getClass().getDeclaredMethod("setTrainers", Set.class);
        setTrainers.setAccessible(true);
        setTrainers.invoke(trainee, trainersSet);
        when(traineeRepository.findByUsername(username)).thenReturn(Optional.of(trainee));

        traineeService.deleteProfile(username);

        for (Trainer trainer : trainersSet) {
            if (trainer.getTrainees().isEmpty()) {
                continue;
            }
            assertThat(trainer.getTrainees()).doesNotContain(trainee);
        }
        verify(traineeRepository, times(1)).delete(trainee);
    }
}
