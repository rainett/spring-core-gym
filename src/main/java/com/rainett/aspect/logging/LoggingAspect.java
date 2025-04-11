package com.rainett.aspect.logging;

import com.rainett.aspect.logging.impl.DefaultLogger;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private final Map<Class<? extends Annotation>, MethodLogger> loggers;
    private final DefaultLogger defaultLogger;

    public LoggingAspect(List<AnnotationAwareLogger> loggers, DefaultLogger defaultLogger) {
        this.loggers = loggers.stream()
                .collect(Collectors.toMap(AnnotationAwareLogger::annotationClass,
                        Function.identity()));
        this.defaultLogger = defaultLogger;
    }

    @Around("@annotation(com.rainett.annotations.Loggable) || within(@com.rainett.annotations.Loggable *)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> targetClass = joinPoint.getTarget().getClass();
        for (Annotation annotation : targetClass.getDeclaredAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (loggers.containsKey(annotationType)) {
                return loggers.get(annotationType).log(joinPoint);
            }
        }
        return defaultLogger.log(joinPoint);
    }
}
