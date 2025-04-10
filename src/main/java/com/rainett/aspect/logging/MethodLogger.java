package com.rainett.aspect.logging;

import org.aspectj.lang.ProceedingJoinPoint;

public interface MethodLogger {
    Object log(ProceedingJoinPoint joinPoint) throws Throwable;
}
