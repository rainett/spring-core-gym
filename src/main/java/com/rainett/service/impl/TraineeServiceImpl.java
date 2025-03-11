package com.rainett.service.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.service.TraineeService;
import com.rainett.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeServiceImpl implements TraineeService {
    @Autowired
    private TraineeDao traineeDao;

    @Autowired
    private UserService userService;

    @Override
    public Trainee createProfile(Trainee trainee) {
        log.info("Creating trainee profile for: {}", trainee);
        String username = userService
                .generateUniqueUsername(trainee.getFirstName(), trainee.getLastName());
        trainee.setUsername(username);
        trainee.setPassword(userService.generateRandomPassword());
        return traineeDao.save(trainee);
    }

    @Override
    public Trainee updateProfile(Trainee trainee) {
        log.info("Updating trainee profile for: {}", trainee);
        if (traineeDao.findByUserId(trainee.getUserId()) == null) {
            throw new IllegalArgumentException("Trainee not found");
        }
        return traineeDao.save(trainee);
    }

    @Override
    public void deleteProfile(Long userId) {
        log.info("Deleting trainee profile for: {}", userId);
        traineeDao.deleteByUserId(userId);
    }

    @Override
    public Trainee getProfile(Long userId) {
        log.info("Getting trainee profile for: {}", userId);
        return traineeDao.findByUserId(userId);
    }

    @Override
    public List<Trainee> getAll() {
        log.info("Getting all trainees");
        return traineeDao.findAll();
    }
}
