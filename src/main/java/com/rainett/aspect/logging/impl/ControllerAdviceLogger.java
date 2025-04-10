package com.rainett.aspect.logging.impl;

import com.rainett.aspect.logging.AnnotationAwareLogger;
import com.rainett.dto.ErrorResponse;
import java.lang.annotation.Annotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Component
public class ControllerAdviceLogger implements AnnotationAwareLogger {
    @Override
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object result = joinPoint.proceed();
        int status = 400;
        String message = "None";
        if (result instanceof ResponseEntity<?> responseEntity) {
            status = responseEntity.getStatusCode().value();
            Object body = responseEntity.getBody();
            if (body instanceof ErrorResponse errorResponse) {
                message = errorResponse.message();
            }
        }
        log.info("<- [{}#{}] returned [{}] with message: {}", className, methodName, status,
                message);
        return result;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RestControllerAdvice.class;
    }
}
