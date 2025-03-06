package com.rainett.service.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.service.TraineeService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private static final String USERNAME_FORMATTER = "%s.%s";
    private static final int PASSWORD_LENGTH = 10;
    private final TraineeDao traineeDao;

    @Override
    public void createProfile(Trainee trainee) {
        String username = generateUniqueUsername(trainee);
        trainee.setUsername(username);
        trainee.setPassword(generateRandomPassword());
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

    private String generateUniqueUsername(Trainee trainee) {
        String initialUsername = String.format(USERNAME_FORMATTER,
                trainee.getFirstName(), trainee.getLastName());
        StringBuilder username = new StringBuilder(initialUsername);
        int number = 1;
        while (traineeDao.usernameExists(username.toString())) {
            username = new StringBuilder(initialUsername).append(".").append(number);
            number++;
        }
        return username.toString();
    }

    private String generateRandomPassword() {
        return Stream.generate(() -> (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1))
                .map(Object::toString)
                .limit(PASSWORD_LENGTH)
                .collect(Collectors.joining());
    }
}
