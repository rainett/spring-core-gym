package com.rainett.controller;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid LoginRequest request) {
        userService.login(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{username}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable String username,
                                               @Valid UpdatePasswordRequest request) {
        userService.updatePassword(username, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable String username,
                                              @Valid UpdateUserActiveRequest request) {
        userService.updateStatus(username, request);
        return ResponseEntity.ok().build();
    }
}
