package com.rainett.repository;

import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.model.TrainingType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findByName(String name);

    @Query("SELECT new com.rainett.dto.trainingtype.TrainingTypeResponse(" +
           "tt.id, tt.name) " +
           "FROM TrainingType tt")
    List<TrainingTypeResponse> findAllDto();
}
