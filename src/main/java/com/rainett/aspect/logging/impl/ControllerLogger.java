package com.rainett.aspect.logging.impl;

import com.rainett.aspect.logging.AnnotationAwareLogger;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Component
public class ControllerLogger implements AnnotationAwareLogger {
    @Override
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        Object[] args = joinPoint.getArgs();
        log.info("-> [{}#{}] called with args: {}", className, methodName, Arrays.toString(args));
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable ex) {
            log.info("<- [{}#{}] threw exception {}: {}", className,
                    methodName, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
        int status = 200;
        if (result instanceof ResponseEntity<?> responseEntity) {
            status = responseEntity.getStatusCode().value();
        }
        log.info("<- [{}#{}] returned [{}]", className, methodName, status);
        return result;
    }

    @Override
    public Class<? extends Annotation> annotationClass() {
        return RestController.class;
    }
}
