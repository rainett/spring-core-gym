package com.rainett.repository;

import com.rainett.model.TrainingType;
import java.util.Optional;

public interface TrainingTypeRepository extends GenericRepository<TrainingType> {
    Optional<TrainingType> findByName(String name);
}
