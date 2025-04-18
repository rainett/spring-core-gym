package com.rainett.controller;

import com.rainett.annotations.Authenticated;
import com.rainett.annotations.Loggable;
import com.rainett.annotations.openapi.NotFoundResponse;
import com.rainett.annotations.openapi.OkResponse;
import com.rainett.annotations.openapi.SecuredOperation;
import com.rainett.annotations.openapi.ValidationResponse;
import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

    @ValidationResponse
    @SecuredOperation
    @OkResponse(description = "User logged in successfully")
    @Operation(
            summary = "User Login",
            description = "Authenticates a user based on Basic Authorization"
    )
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        userService.login(request);
        return ResponseEntity.ok().build();
    }

    @ValidationResponse
    @SecuredOperation
    @NotFoundResponse(description = "User not found")
    @OkResponse(description = "Password updated successfully")
    @Operation(
            summary = "Update password",
            description = "Updates a user's password"
    )
    @PutMapping("/{username}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable("username") String username,
                                               @Valid @RequestBody UpdatePasswordRequest request) {
        userService.updatePassword(username, request);
        return ResponseEntity.ok().build();
    }

    @ValidationResponse
    @SecuredOperation
    @NotFoundResponse(description = "User not found")
    @OkResponse(description = "Status updated successfully")
    @Operation(
            summary = "Update status",
            description = "Updates a user's status"
    )
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("username") String username,
                                             @Valid @RequestBody UpdateUserActiveRequest request) {
        userService.updateStatus(username, request);
        return ResponseEntity.ok().build();
    }
}
