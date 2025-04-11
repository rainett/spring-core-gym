package com.rainett.service.impl;

import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.repository.TrainingTypeRepository;
import com.rainett.service.TrainingTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public List<TrainingTypeResponse> getTrainingTypes() {
        return trainingTypeRepository.findAllDto();
    }
}
