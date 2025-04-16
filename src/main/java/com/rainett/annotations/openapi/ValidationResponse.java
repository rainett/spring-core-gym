package com.rainett.annotations.openapi;

import com.rainett.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "422", description = "Validation error",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                mediaType = "application/json"))
public @interface ValidationResponse {
    @AliasFor(annotation = ApiResponse.class, attribute = "description")
    String description() default "Validation error";
}
