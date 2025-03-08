package com.rainett.service.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.service.TraineeService;
import com.rainett.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDao traineeDao;
    private final UserService userService;

    @Override
    public void createProfile(Trainee trainee) {
        String username = userService
                .generateUniqueUsername(trainee.getFirstName(), trainee.getLastName());
        trainee.setUsername(username);
        trainee.setPassword(userService.generateRandomPassword());
        traineeDao.save(trainee);
    }

    @Override
    public void updateProfile(Trainee trainee) {
        if (traineeDao.findByUserId(trainee.getUserId()) == null) {
            throw new IllegalArgumentException("Trainee not found");
        }
        traineeDao.save(trainee);
    }

    @Override
    public void deleteProfile(Long userId) {
        traineeDao.deleteByUserId(userId);
    }

    @Override
    public Trainee getProfile(Long userId) {
        return traineeDao.findByUserId(userId);
    }
}
