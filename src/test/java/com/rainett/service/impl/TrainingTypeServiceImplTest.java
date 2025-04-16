package com.rainett.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.repository.TrainingTypeRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TrainingTypeServiceImplTest {
    @Test
    @DisplayName("Get all training types")
    void test() {
        TrainingTypeRepository repository = mock(TrainingTypeRepository.class);
        TrainingTypeServiceImpl service = new TrainingTypeServiceImpl(repository);
        List<TrainingTypeResponse> trainingTypes =
                List.of(new TrainingTypeResponse(), new TrainingTypeResponse());
        when(repository.findAllDto()).thenReturn(trainingTypes);

        List<TrainingTypeResponse> result = service.getTrainingTypes();
        assertThat(result).containsExactlyInAnyOrderElementsOf(trainingTypes);
    }
}