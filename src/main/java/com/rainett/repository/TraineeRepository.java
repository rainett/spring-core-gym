package com.rainett.repository;

import com.rainett.model.Trainee;
import java.util.Optional;

public interface TraineeRepository extends GenericRepository<Trainee> {
    Optional<Trainee> findByUsername(String username);
}
