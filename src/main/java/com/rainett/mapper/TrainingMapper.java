package com.rainett.mapper;

import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.model.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    @Mapping(target = "trainingType", ignore = true)
    Training toEntity(CreateTrainingRequest request);
}
