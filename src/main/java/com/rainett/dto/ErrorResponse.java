package com.rainett.dto;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String message) {
}
