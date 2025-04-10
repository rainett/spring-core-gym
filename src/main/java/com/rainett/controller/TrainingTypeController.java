package com.rainett.controller;

import com.rainett.annotations.Authenticated;
import com.rainett.annotations.Loggable;
import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.service.TrainingTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Loggable
@Authenticated
@RestController
@RequestMapping("/api/training-types")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @GetMapping
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> trainingTypesResponse = trainingTypeService.getTrainingTypes();
        return ResponseEntity.ok(trainingTypesResponse);
    }
}
