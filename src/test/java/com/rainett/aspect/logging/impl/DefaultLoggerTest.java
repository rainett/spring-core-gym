package com.rainett.aspect.logging.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

@ExtendWith(MockitoExtension.class)
class DefaultLoggerTest {
    private final DefaultLogger defaultLogger = new DefaultLogger();

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp() {
        when(joinPoint.getSignature()).thenReturn(methodSignature);
    }

    @Test
    void testDefaultLogger() throws Throwable {
        when(methodSignature.getDeclaringType()).thenReturn(DummyController.class);
        when(methodSignature.getName()).thenReturn("testMethod");
        Object[] args = new Object[] {"arg1", 42};
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn("defaultResult");
        Object result = defaultLogger.log(joinPoint);
        assertEquals("defaultResult", result);
        verify(joinPoint, times(1)).proceed();
    }

    static class DummyController {}
}