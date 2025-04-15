package com.rainett.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.exceptions.AuthenticationException;
import com.rainett.exceptions.LoginException;
import com.rainett.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@WebMvcTest(GlobalExceptionHandlerTest.DummyController.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DummyController dummyController;

    @Test
    @DisplayName("Handles validation errors")
    void handlesValidationErrors() throws Exception {
        CustomDto request = new CustomDto("something");
        mockMvc.perform(get("/api/dummy/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Handles constraint violations")
    void handlesConstraintViolations() throws Exception {
        doThrow(new ConstraintViolationException("", Set.of())).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Handles login exception")
    void handleLoginException() throws Exception {
        doThrow(new LoginException()).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Handles authentication exception")
    void handleAuthException() throws Exception {
        doThrow(new AuthenticationException(500, "Error")).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Handles entity not found exception")
    void handleEntityNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Error")).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Handles data integrity exception")
    void handleDataIntegrityException() throws Exception {
        doThrow(new DataIntegrityViolationException("Error")).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Handles method not supported exception")
    void handleMethodNotSupportedException() throws Exception {
        doThrow(new HttpRequestMethodNotSupportedException("Error")).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Handles no handler found exception")
    void handleNoHandlerFoundException() throws Exception {
        doThrow(new NoHandlerFoundException("GET", "/api/dummy", new HttpHeaders())).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Handles media type not supported exception")
    void handleMediaTypeNotSupportedException() throws Exception {
        doThrow(new HttpMediaTypeNotSupportedException("Error")).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Handles no resource found exception")
    void handleNoResourceFoundException() throws Exception {
        doThrow(new NoResourceFoundException(HttpMethod.GET, "/api/dummy")).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Handles illegal argument exception")
    void handleIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException()).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Handles internal server error")
    void handleInternalServerError() throws Exception {
        doThrow(new Exception()).when(dummyController).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isInternalServerError());
    }

    @RestController
    @RequestMapping("/api/dummy")
    static class DummyController {
        @GetMapping("/valid")
        public void valid(@Valid CustomDto dto) {
            // validated endpoint
        }

        @GetMapping
        public void empty() throws Exception {
            // empty endpoint
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CustomDto {
        @NotBlank
        private String name;
    }
}