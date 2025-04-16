package com.rainett.mapper;

import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    @Mapping(target = "specialization", ignore = true)
    Trainer toEntity(CreateTrainerRequest request);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    void updateEntity(@MappingTarget Trainer trainer, UpdateTrainerRequest request);

    TrainerResponse toDto(Trainer trainer);

    TrainerDto toTrainerDto(Trainee trainee);

    default String map(TrainingType trainingType) {
        return trainingType.getName();
    }
}
