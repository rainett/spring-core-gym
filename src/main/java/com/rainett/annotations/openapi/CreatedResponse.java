package com.rainett.annotations.openapi;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "201",
        description = "Created",
        content = @Content
)
public @interface CreatedResponse {
    @AliasFor(annotation = ApiResponse.class, attribute = "content")
    Content content() default @Content;

    @AliasFor(annotation = ApiResponse.class, attribute = "description")
    String description() default "Created";
}
