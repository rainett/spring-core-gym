package com.rainett.mapper;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.model.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    Trainee toEntity(CreateTraineeRequest request);

    @Mapping(target = "username", ignore = true)
    void updateEntity(@MappingTarget Trainee trainee, UpdateTraineeRequest request);
}
