package com.rainett.service.impl;

import com.rainett.dto.user.UpdateUserRequest;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.service.UserService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String USERNAME_FORMATTER = "%s.%s";
    private static final String NUMERIC_USERNAME_FORMATTER = "%s.%d";
    private static final int PASSWORD_LENGTH = 10;

    private final UserRepository userRepository;

    @Override
    public boolean usernameRequiresUpdate(User user, UpdateUserRequest userDto) {
        return !userDto.getFirstName().equals(user.getFirstName()) ||
               !userDto.getLastName().equals(user.getLastName());
    }

    @Override
    public String generateUsername(String firstName, String lastName) {
        log.info("Generating unique username for {} {}", firstName, lastName);
        String initialUsername = String.format(USERNAME_FORMATTER, firstName, lastName);
        long totalSuffixes = userRepository.findSuffixUsernameCount(initialUsername);
        if (totalSuffixes == 0) {
            return initialUsername;
        }
        return String.format(NUMERIC_USERNAME_FORMATTER, initialUsername, totalSuffixes);
    }

    @Override
    public String generatePassword() {
        log.info("Generating random password");
        return Stream.generate(() -> (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1))
                .limit(PASSWORD_LENGTH)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
