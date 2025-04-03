package com.rainett.mapper;

import com.rainett.dto.trainer.CreateTrainerProfileRequest;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.model.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    @Mapping(target = "specialization", ignore = true)
    Trainer toEntity(CreateTrainerProfileRequest request);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    void updateEntity(@MappingTarget Trainer trainer, UpdateTrainerRequest request);
}
