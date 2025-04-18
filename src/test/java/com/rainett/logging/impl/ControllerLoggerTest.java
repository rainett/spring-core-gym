package com.rainett.logging.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ControllerLoggerTest {
    private final ControllerLogger controllerLogger = new ControllerLogger();

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp() {
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(DummyController.class);
        when(methodSignature.getName()).thenReturn("controllerMethod");
        Object[] args = new Object[] {"param"};
        when(joinPoint.getArgs()).thenReturn(args);
    }

    @Test
    void testControllerLogger_Success() throws Throwable {
        ResponseEntity<String> response = ResponseEntity.ok("ok");
        when(joinPoint.proceed()).thenReturn(response);
        Object result = controllerLogger.log(joinPoint);
        assertSame(response, result);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void testControllerLogger_Exception() throws Throwable {
        RuntimeException ex = new RuntimeException("test exception");
        when(joinPoint.proceed()).thenThrow(ex);
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> controllerLogger.log(joinPoint));
        assertEquals("test exception", thrown.getMessage());
        verify(joinPoint, times(1)).proceed();
    }

    static class DummyController {}
}