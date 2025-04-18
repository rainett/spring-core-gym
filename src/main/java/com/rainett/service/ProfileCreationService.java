package com.rainett.service;

import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileCreationService<T extends User> {
    UserCredentialsResponse createProfile(T user, JpaRepository<T, Long> repository);
}
