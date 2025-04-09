package com.rainett.listener;

import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Slf4j
@Component
public class UserListener {
    private static final String USERNAME_FORMATTER = "%s.%s";
    private static final int PASSWORD_LENGTH = 10;

    private UserRepository userRepository;
    private boolean wasContextInitialized = false;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PrePersist
    public void prePersist(User user) {
        log.info("Generating username and password for user {}", user);
        initContext();
        user.setUsername(generateUsername(user.getFirstName(), user.getLastName()));
        user.setPassword(generatePassword());
    }

    @PreUpdate
    public void preUpdate(User user) {
        initContext();
        if (user.isUsernameRequiresUpdate()) {
            String username = generateUsername(user.getFirstName(), user.getLastName());
            user.setUsername(username);
        }
    }

    private void initContext() {
        if (!wasContextInitialized) {
            SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
            wasContextInitialized = true;
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
