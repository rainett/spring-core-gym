package com.rainett.mapper;

import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.model.Training;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
    Training toEntity(CreateTrainingRequest request);
}
