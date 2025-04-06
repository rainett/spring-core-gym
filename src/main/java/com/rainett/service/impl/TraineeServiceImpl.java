package com.rainett.service.impl;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainerDto;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.exceptions.EntityNotFoundException;
import com.rainett.mapper.TraineeMapper;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.repository.TraineeRepository;
import com.rainett.repository.TrainerRepository;
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
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final TraineeMapper traineeMapper;

    @Override
    @Transactional(readOnly = true)
    public TraineeResponse findByUsername(String username) {
        return traineeRepository.findTraineeDtoByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainee not found for username = [" + username + "]"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainingsResponse> findTrainings(String username,
                                                        LocalDate from,
                                                        LocalDate to,
                                                        String trainerUsername,
                                                        String trainingType) {
        return traineeRepository.findTraineeTrainingsDto(username, from, to, trainerUsername,
                trainingType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TraineeTrainerDto> findUnassignedTrainers(String username) {
        return traineeRepository.findUnassignedTrainersDto(username);
    }

    @Override
    @Transactional
    public UserCredentialsResponse createProfile(CreateTraineeRequest request) {
        Trainee trainee = traineeMapper.toEntity(request);
        trainee.setIsActive(true);
        trainee = traineeRepository.save(trainee);
        return new UserCredentialsResponse(trainee.getUsername(), trainee.getPassword());
    }

    @Override
    @Transactional
    public TraineeResponse updateTrainee(String username, UpdateTraineeRequest request) {
        Trainee trainee = getTrainee(username);
        traineeMapper.updateEntity(trainee, request);
        trainee.setActiveUpdatedAt(LocalDateTime.now());
        return traineeMapper.toDto(trainee);
    }

    @Override
    @Transactional
    public List<TraineeTrainerDto> updateTrainers(String username, UpdateTraineeTrainersRequest request) {
        Trainee trainee = getTrainee(username);
        List<Trainer> trainers = trainerRepository.findByUsernames(request.getTrainersUsernames());
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

    private Trainee getTrainee(String username) {
        return traineeRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Trainee not found for username = [" + username + "]"));
    }
}
