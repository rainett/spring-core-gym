package com.rainett.service.impl;

import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.service.CredentialsGenerationService;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialsGenerationServiceImpl implements CredentialsGenerationService {
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

    private String generateUsername(String firstName, String lastName) {
        String initialUsername = String.format(USERNAME_FORMATTER, firstName, lastName);
        List<String> taken = userRepository.findUsernamesByInitialUsername(initialUsername);
        if (taken.stream().noneMatch(u -> u.equalsIgnoreCase(initialUsername))) {
            return initialUsername;
        }
        int maxSuffix = taken.stream()
                .map(u -> getMaxSuffix(u, initialUsername))
                .max(Integer::compare)
                .orElse(0);
        return String.format(USERNAME_FORMATTER, initialUsername, maxSuffix + 1);
    }

    private static int getMaxSuffix(String u, String initialUsername) {
        String after = u.substring(initialUsername.length());
        if (after.startsWith(".")) {
            try {
                return Integer.parseInt(after.substring(1));
            } catch (NumberFormatException e) { /* fall through */ }
        }
        return 0;
    }

    private String generatePassword() {
        return Stream.generate(() -> (char) ThreadLocalRandom.current().nextInt('a', 'z' + 1))
                .limit(PASSWORD_LENGTH)
                .map(Object::toString)
                .collect(Collectors.joining());
    }
}
