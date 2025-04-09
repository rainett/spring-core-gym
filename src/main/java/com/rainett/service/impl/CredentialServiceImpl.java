package com.rainett.service.impl;

import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.service.CredentialService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {
    private static final String USERNAME_FORMATTER = "%s.%s";

    private static final int PASSWORD_LENGTH = 10;
    private final UserRepository userRepository;

    @Override
    public void createCredentials(User user) {
        String username = generateUsername(user.getFirstName(), user.getLastName());
        String password = generatePassword();
        user.setUsername(username);
        user.setPassword(password);
    }

    @Override
    public void updateCredentials(User user, String firstName, String lastName) {
        if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName)) {
            return;
        }
        String username = generateUsername(firstName, lastName);
        user.setUsername(username);
    }

    private String generateUsername(String firstName, String lastName) {
        String initialUsername = String.format(USERNAME_FORMATTER, firstName, lastName);
        long totalSuffixes = userRepository.findSuffixUsernameCount(initialUsername);
        if (totalSuffixes == 0) {
            return initialUsername;
        }
        return String.format(USERNAME_FORMATTER, initialUsername, totalSuffixes);
    }

    private String generatePassword() {
        return Stream.generate(() -> (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1))
                .limit(PASSWORD_LENGTH)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
