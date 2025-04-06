package com.rainett.listener.impl;

import com.rainett.listener.UserListener;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserListenerImpl implements UserListener {
    private static final String USERNAME_FORMATTER = "%s.%s";
    private static final int PASSWORD_LENGTH = 10;

    private final UserRepository userRepository;

    @Override
    @PrePersist
    public void prePersist(User user) {
        user.setUsername(generateUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());
    }

    @Override
    @PreUpdate
    public void preUpdate(User user) {
        if (user.isUsernameRequiresUpdate()) {
            String username = generateUsername(user.getFirstName(), user.getLastName());
            user.setUsername(username);
        }
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
