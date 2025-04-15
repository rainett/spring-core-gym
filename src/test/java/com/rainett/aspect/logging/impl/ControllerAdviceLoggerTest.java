package com.rainett.aspect.logging.impl;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.aspect.logging.MethodLogger;
import com.rainett.dto.ErrorResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ControllerAdviceLoggerTest {
    private final ControllerAdviceLogger logger = new ControllerAdviceLogger();

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp() {
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(DummyControllerAdvice.class);
        when(methodSignature.getName()).thenReturn("adviceMethod");
    }

    @Test
    void testControllerAdviceLogger_WithErrorResponse() throws Throwable {
        ErrorResponse errorResponse = new ErrorResponse(null, 400, null, null);
        ResponseEntity<ErrorResponse> responseEntity =
                new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        when(joinPoint.proceed()).thenReturn(responseEntity);
        Object result = logger.log(joinPoint);
        assertSame(responseEntity, result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void testControllerAdviceLogger_WithoutErrorResponse() throws Throwable {
        ResponseEntity<String> responseEntity =
                new ResponseEntity<>("All good", HttpStatus.OK);
        when(joinPoint.proceed()).thenReturn(responseEntity);
        MethodLogger adviceLogger = new ControllerAdviceLogger();
        Object result = adviceLogger.log(joinPoint);
        assertSame(responseEntity, result);
        verify(joinPoint, times(1)).proceed();
    }

    static class DummyControllerAdvice {
    }
}