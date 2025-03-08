package com.rainett.service.impl;

import com.rainett.service.UserService;
import com.rainett.storage.DataStorage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USERNAME_FORMATTER = "%s.%s";
    private static final int PASSWORD_LENGTH = 10;
    private final DataStorage dataStorage;

    @Override
    public String generateUniqueUsername(String firstName, String lastName) {
        String initialUsername = String.format(USERNAME_FORMATTER, firstName, lastName);
        StringBuilder username = new StringBuilder(initialUsername);
        int number = 1;
        while (dataStorage.usernameExists(username.toString())) {
            username = new StringBuilder(initialUsername).append(".").append(number);
            number++;
        }
        return username.toString();
    }

    @Override
    public String generateRandomPassword() {
        return Stream.generate(() -> (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1))
                .limit(PASSWORD_LENGTH)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
