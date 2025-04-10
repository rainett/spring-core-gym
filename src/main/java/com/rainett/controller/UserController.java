package com.rainett.controller;

import com.rainett.annotations.Authenticated;
import com.rainett.annotations.Loggable;
import com.rainett.dto.ErrorResponse;
import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "Endpoints for managing users")
@Loggable
@Authenticated
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "User Login",
            description = "Authenticates a user, sends an error if authentication fails",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "400", description = "Invalid auth header",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class),
                                    mediaType = "application/json"))
            },
            security = {
                    @SecurityRequirement(name = "basicAuth") // to implement
            }
    )
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        userService.login(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("username") String username,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(username, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("username") String username,
                                             @Valid @RequestBody UpdateUserActiveRequest request) {
        userService.updateStatus(username, request);
        return ResponseEntity.ok().build();
    }
}
