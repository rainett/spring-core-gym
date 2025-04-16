package com.rainett.service;

import com.rainett.dto.trainingtype.TrainingTypeResponse;
import java.util.List;

public interface TrainingTypeService {
    List<TrainingTypeResponse> getTrainingTypes();
}
