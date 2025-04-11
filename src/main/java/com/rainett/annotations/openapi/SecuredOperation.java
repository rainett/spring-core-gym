package com.rainett.annotations.openapi;

import com.rainett.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "400", description = "Invalid auth header",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)))
@ApiResponse(responseCode = "401", description = "Unauthorized",
        content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)))
@SecurityRequirement(name = "basicAuth")
public @interface SecuredOperation {
}
