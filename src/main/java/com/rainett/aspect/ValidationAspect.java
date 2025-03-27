package com.rainett.aspect;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ValidationAspect {
    private final Validator validator;

    @Around("execution(* *(.., @jakarta.validation.Valid (*), ..))")
    public Object validateMethodParameters(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Method method = getImplementationMethod(target, signature.getMethod());
        Object[] arguments = joinPoint.getArgs();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (validateMethodCall(parameterAnnotations, arguments)) {
            return joinPoint.proceed();
        }
        return null;
    }

    private Method getImplementationMethod(Object target, Method interfaceMethod)
            throws NoSuchMethodException {
        return target.getClass()
                .getMethod(interfaceMethod.getName(), interfaceMethod.getParameterTypes());
    }

    private boolean validateMethodCall(Annotation[][] parameterAnnotations, Object[] arguments) {
        Set<ConstraintViolation<Object>> violations = new HashSet<>();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType().equals(Valid.class)) {
                    Object argument = arguments[i];
                    violations.addAll(validator.validate(argument));
                    break;
                }
            }
        }
        if (!violations.isEmpty()) {
            log.warn("Validation errors: {}", violations);
            return false;
        }
        return true;
    }
}