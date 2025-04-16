package com.rainett.dto;


import lombok.Getter;

public record AuthResult(Status status, String message) {
    @Getter
    public enum Status {
        SUCCESS(200), INVALID_HEADER(400), UNAUTHORIZED(401);
        private final int code;

        Status(int code) {
            this.code = code;
        }
    }
}
