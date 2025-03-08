package com.rainett.service.impl;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.service.TrainerService;
import com.rainett.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDao trainerDao;
    private final UserService userService;

    @Override
    public void createProfile(Trainer trainer) {
        String username = userService
                .generateUniqueUsername(trainer.getFirstName(), trainer.getLastName());
        trainer.setUsername(username);
        trainer.setPassword(userService.generateRandomPassword());
        trainerDao.save(trainer);
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
