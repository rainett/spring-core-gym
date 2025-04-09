package com.rainett.repository;

import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.model.Trainer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    List<Trainer> findByUsernameIn(List<String> usernames);

    Optional<Trainer> findByUsername(String username);

    @Query("SELECT new com.rainett.dto.trainer.TrainerResponse(" +
           "t.username, t.firstName, t.lastName, t.specialization.name, t.isActive, null) " +
           "FROM Trainer t WHERE t.username = :username")
    Optional<TrainerResponse> findTrainerDtoByUsername(@Param("username") String trainerUsername);

    @Query("SELECT new com.rainett.dto.trainer.TrainerTrainingResponse(" +
           "training.name, training.date, training.trainingType.name, training.duration, trainee.username) " +
           "FROM Trainer trainer " +
           "JOIN trainer.trainings training " +
           "JOIN training.trainee trainee " +
           "WHERE trainer.username = :username " +
           "  AND (:from IS NULL OR training.date >= :from) " +
           "  AND (:to IS NULL OR training.date <= :to) " +
           "  AND (:traineeUsername IS NULL OR trainee.username = :traineeUsername)")
    List<TrainerTrainingResponse> findTrainerTrainingsDto(
            @Param("username") String trainerUsername,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("traineeUsername") String traineeUsername);

    @Query("SELECT new com.rainett.dto.trainee.TrainerDto(" +
           "t.username, t.firstName, t.lastName, t.specialization.name) " +
           "FROM Trainer t " +
           "JOIN t.trainees trainee " +
           "WHERE trainee.username = :username")
    List<TrainerDto> findTrainersDtoForTrainee(@Param("username") String traineeUsername);

    boolean existsByUsername(String username);
}
