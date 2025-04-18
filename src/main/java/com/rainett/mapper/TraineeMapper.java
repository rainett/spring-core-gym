package com.rainett.mapper;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    Trainee toEntity(CreateTraineeRequest request);

    void updateEntity(@MappingTarget Trainee trainee, UpdateTraineeRequest request);

    TraineeResponse toDto(Trainee trainee);

    TrainerDto toTrainerDto(Trainer trainer);

    default String map(TrainingType trainingType) {
        return trainingType.getName();
    }
}
