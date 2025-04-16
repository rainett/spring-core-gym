package com.rainett.controller;

import com.rainett.annotations.Authenticated;
import com.rainett.annotations.Loggable;
import com.rainett.annotations.openapi.OkResponse;
import com.rainett.annotations.openapi.SecuredOperation;
import com.rainett.dto.trainingtype.TrainingTypeResponse;
import com.rainett.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Training Types API", description = "Endpoints for managing training types")
@Loggable
@Authenticated
@RestController
@RequestMapping("/api/training-types")
@RequiredArgsConstructor
public class TrainingTypeController {
    private final TrainingTypeService trainingTypeService;

    @SecuredOperation
    @OkResponse(description = "Training types found successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(implementation = TrainingTypeResponse.class)
                    )
            )
    )
    @Operation(
            summary = "Get training types",
            description = "Retrieves all training types"
    )
    @GetMapping
    public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
        List<TrainingTypeResponse> trainingTypesResponse = trainingTypeService.getTrainingTypes();
        return ResponseEntity.ok(trainingTypesResponse);
    }
}
