package com.rainett.repository;

import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.model.TrainingType;
import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository extends GenericRepository<TrainingType> {
    Optional<TrainingType> findByName(String name);

    List<TrainingTypeResponse> findAllDto();
}
