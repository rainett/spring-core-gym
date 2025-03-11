package com.rainett.service.impl;

import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.User;
import com.rainett.service.UserService;
import com.rainett.storage.DataStorage;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    private static final String USERNAME_FORMATTER = "%s.%s";
    private static final int PASSWORD_LENGTH = 10;

    @Autowired
    private DataStorage<User> dataStorage;

    @Override
    public String generateUniqueUsername(String firstName, String lastName) {
        String initialUsername = String.format(USERNAME_FORMATTER, firstName, lastName);
        StringBuilder username = new StringBuilder(initialUsername);
        int number = 1;
        while (usernameExists(username.toString())) {
            username = new StringBuilder(initialUsername).append(".").append(number);
            number++;
        }
        return username.toString();
    }

    private boolean usernameExists(String string) {
        List<User> users = dataStorage.findAll(Trainer.class);
        users.addAll(dataStorage.findAll(Trainee.class));
        return users.stream().anyMatch(user -> user.getUsername().equals(string));
    }

    @Override
    public String generateRandomPassword() {
        return Stream.generate(() -> (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1))
                .limit(PASSWORD_LENGTH)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
