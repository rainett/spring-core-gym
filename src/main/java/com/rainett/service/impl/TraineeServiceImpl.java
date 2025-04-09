package com.rainett.service.impl;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.mapper.TraineeMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
import com.rainett.service.CredentialService;
import com.rainett.service.TraineeService;
import java.time.LocalDate;
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
    private static final String NO_TRAINEE_MESSAGE = "Trainee not found for username = [%s]";

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper traineeMapper;
    private final CredentialService credentialService;

    @Override
    @Transactional(readOnly = true)
    public TraineeResponse findByUsername(String username) {
        TraineeResponse traineeResponse = traineeRepository.findTraineeDtoByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(getNoTraineeMessage(username)));
        traineeResponse.setTrainers(trainerRepository.findTrainersDtoForTrainee(username));
        return traineeResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingsResponse> findTrainings(String username,
                                                        LocalDate from,
                                                        LocalDate to,
                                                        String trainerUsername,
                                                        String trainingType) {
        if (!traineeRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException(getNoTraineeMessage(username));
        }
        return traineeRepository.findTraineeTrainingsDto(username, from, to, trainerUsername,
                trainingType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainerDto> findUnassignedTrainers(String username) {
        if (!traineeRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException(getNoTraineeMessage(username));
        }
        return traineeRepository.findUnassignedTrainersDto(username);
    }

    @Override
    @Transactional
    public UserCredentialsResponse createProfile(CreateTraineeRequest request) {
        Trainee trainee = traineeMapper.toEntity(request);
        trainee.setIsActive(true);
        credentialService.createCredentials(trainee);
        trainee = traineeRepository.save(trainee);
        return new UserCredentialsResponse(trainee.getUsername(), trainee.getPassword());
    }

    @Override
    @Transactional
    public TraineeResponse updateTrainee(String username, UpdateTraineeRequest request) {
        Trainee trainee = getTrainee(username);
        credentialService.updateCredentials(trainee, request.getFirstName(), request.getLastName());
        traineeMapper.updateEntity(trainee, request);
        trainee.setActiveUpdatedAt(LocalDateTime.now());
        return traineeMapper.toDto(trainee);
    }

    @Override
    @Transactional
    public List<TrainerDto> updateTrainers(String username, UpdateTraineeTrainersRequest request) {
        Trainee trainee = getTrainee(username);
        List<Trainer> trainers = trainerRepository.findByUsernameIn(request.getTrainersUsernames());
        trainee.updateTrainers(trainers);
        return trainee.getTrainers().stream()
                .map(traineeMapper::toTrainerDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteProfile(String username) {
        Trainee trainee = getTrainee(username);
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee);
        }
        traineeRepository.delete(trainee);
    }

    private static String getNoTraineeMessage(String username) {
        return String.format(NO_TRAINEE_MESSAGE, username);
    }

    private Trainee getTrainee(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(getNoTraineeMessage(username)));
    }
}
