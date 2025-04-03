package com.rainett.repository;

import com.rainett.model.User;
import java.util.Optional;

public interface UserRepository {
    long findSuffixUsernameCount(String initialUsername);

    Optional<User> findByUsername(String username);
}
