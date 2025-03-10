package com.rainett.service.impl;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.service.TrainerService;
import com.rainett.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    @Autowired
    private UserService userService;

    @Override
    public Long createProfile(Trainer trainer) {
        String username = userService
                .generateUniqueUsername(trainer.getFirstName(), trainer.getLastName());
        trainer.setUsername(username);
        trainer.setPassword(userService.generateRandomPassword());
        return trainerDao.save(trainer);
    }

    @Override
    public void updateProfile(Trainer trainer) {
        if (trainerDao.findByUserId(trainer.getUserId()) == null) {
            throw new IllegalArgumentException("Trainee not found");
        }
        trainerDao.save(trainer);
    }

    @Override
    public Trainer getProfile(Long userId) {
        return trainerDao.findByUserId(userId);
    }
}
