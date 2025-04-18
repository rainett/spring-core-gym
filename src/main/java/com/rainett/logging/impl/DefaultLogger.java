package com.rainett.logging.impl;

import com.rainett.logging.MethodLogger;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultLogger implements MethodLogger {
    @Override
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();
        log.info("-> [{}#{}] called with args: {}", className, methodName, Arrays.toString(args));
        return joinPoint.proceed();
    }
}
