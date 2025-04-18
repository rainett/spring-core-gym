package com.rainett.controller;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.exceptions.LoginException;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.exceptions.TooManyLoginAttemptsException;
import com.rainett.security.JwtFilter;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Import(GlobalExceptionHandlerTest.DummyController.class)
@WebMvcTest(value = GlobalExceptionHandlerTest.DummyController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtFilter.class))
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private DummyController dummyController;

    @MockitoBean
    private DummyService dummyService;

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
        doThrow(new ConstraintViolationException("", Set.of())).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Handles login exception")
    void handleLoginException() throws Exception {
        doThrow(new LoginException()).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Handles too many login attempts exception")
    void handleTooManyLoginAttemptsException() throws Exception {
        doThrow(new TooManyLoginAttemptsException()).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Handles entity not found exception")
    void handleEntityNotFoundException() throws Exception {
        doThrow(new ResourceNotFoundException("Error")).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Handles data integrity exception")
    void handleDataIntegrityException() throws Exception {
        doThrow(new DataIntegrityViolationException("Error")).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Handles method not supported exception")
    void handleMethodNotSupportedException() throws Exception {
        doThrow(new HttpRequestMethodNotSupportedException("Error")).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Handles no handler found exception")
    void handleNoHandlerFoundException() throws Exception {
        doThrow(new NoHandlerFoundException("GET", "/api/dummy", new HttpHeaders())).when(
                dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Handles media type not supported exception")
    void handleMediaTypeNotSupportedException() throws Exception {
        doThrow(new HttpMediaTypeNotSupportedException("Error")).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("Handles no resource found exception")
    void handleNoResourceFoundException() throws Exception {
        doThrow(new NoResourceFoundException(HttpMethod.GET, "/api/dummy")).when(dummyService)
                .empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Handles illegal argument exception")
    void handleIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException()).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Handles internal server error")
    void handleInternalServerError() throws Exception {
        doThrow(new Exception()).when(dummyService).empty();
        mockMvc.perform(get("/api/dummy"))
                .andExpect(status().isInternalServerError());
    }

    @RestController
    @RequestMapping("/api/dummy")
    @RequiredArgsConstructor
    static class DummyController {
        private final DummyService dummyService;

        @GetMapping("/valid")
        public void valid(@Valid CustomDto dto) {
            // validated endpoint
        }

        @GetMapping
        public void empty() throws Exception {
            dummyService.empty();
        }
    }

    @Service
    static class DummyService {
        public void empty() throws Exception {
            // empty method
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