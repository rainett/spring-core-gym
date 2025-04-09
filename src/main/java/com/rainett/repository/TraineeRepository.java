package com.rainett.repository;

import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainer.TraineeDto;
import com.rainett.model.Trainee;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long> {
    Optional<Trainee> findByUsername(String username);

    @Query("SELECT new com.rainett.dto.trainee.TraineeResponse(" +
           "t.username, t.firstName, t.lastName, t.dateOfBirth, t.address, t.isActive, null) " +
           "FROM Trainee t " +
           "WHERE t.username = :username")
    Optional<TraineeResponse> findTraineeDtoByUsername(@Param("username") String traineeUsername);

    @Query("SELECT new com.rainett.dto.trainee.TraineeTrainingsResponse(" +
           "training.name, training.date, training.trainingType.name, training.duration, trainer.username) " +
           "FROM Trainee trainee " +
           "JOIN trainee.trainings training " +
           "JOIN training.trainer trainer " +
           "WHERE trainee.username = :username " +
           "  AND (:from IS NULL OR training.date >= :from) " +
           "  AND (:to IS NULL OR training.date <= :to) " +
           "  AND (:trainerUsername IS NULL OR trainer.username = :trainerUsername) " +
           "  AND (:trainingType IS NULL OR training.trainingType.name = :trainingType)")
    List<TraineeTrainingsResponse> findTraineeTrainingsDto(
            @Param("username") String traineeUsername,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("trainerUsername") String trainerUsername,
            @Param("trainingType") String trainingType);

    @Query("SELECT new com.rainett.dto.trainee.TrainerDto(" +
           "tr.username, tr.firstName, tr.lastName, tr.specialization.name) " +
           "FROM Trainer tr " +
           "WHERE tr.username NOT IN (" +
           "    SELECT trainer.username " +
           "    FROM Trainee t " +
           "    JOIN t.trainers trainer " +
           "    WHERE t.username = :username" +
           ")")
    List<TrainerDto> findUnassignedTrainersDto(@Param("username") String username);

    @Query("SELECT new com.rainett.dto.trainer.TraineeDto(" +
           "t.username, t.firstName, t.lastName) " +
           "FROM Trainee t " +
           "JOIN t.trainers trainer " +
           "WHERE trainer.username = :username")
    List<TraineeDto> findTraineesDtoForTrainer(@Param("username") String trainerUsername);

    boolean existsByUsername(String username);
}
