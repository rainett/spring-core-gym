package com.rainett.repository;

import com.rainett.model.Trainer;
import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends GenericRepository<Trainer> {
    List<Trainer> findByUsernames(List<String> usernames);

    Optional<Trainer> findByUsername(String username);

    List<Trainer> findWithoutTraineeByUsername(String username);
}
