package com.rainett.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.rainett.logging.impl.DefaultLogger;
import java.lang.annotation.Annotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

@SpringBootTest
@Import(LoggingAspectTest.TestConfig.class)
class LoggingAspectTest {

    @Autowired
    private CustomService customService;

    @Autowired
    private DefaultService defaultService;

    @Test
    @DisplayName("Aspect uses custom logger when annotation is present")
    void testCustomLoggerUsed() {
        String result = customService.customMethod();
        assertEquals("loggedByCustom", result);
    }

    @Test
    @DisplayName("Aspect uses default logger when no annotation is present")
    void testDefaultLoggerUsed() {
        String result = defaultService.defaultMethod();
        assertEquals("loggedByDefault", result);
    }

    @Configuration
    @EnableAspectJAutoProxy
    @Import({LoggingAspect.class, CustomService.class, DefaultService.class})
    static class TestConfig {
        @Bean
        public DefaultLogger defaultLogger() {
            return new DefaultLogger() {
                @Override
                public Object log(ProceedingJoinPoint joinPoint) {
                    return "loggedByDefault";
                }
            };
        }

        @Bean
        public AnnotationAwareLogger customLogger() {
            return new AnnotationAwareLogger() {
                @Override
                public Class<? extends Annotation> annotationClass() {
                    return Service.class;
                }

                @Override
                public Object log(ProceedingJoinPoint joinPoint) {
                    return "loggedByCustom";
                }
            };
        }
    }

    @Service
    @Loggable
    static class CustomService {
        public String customMethod() {
            return null;
        }
    }

    @Loggable
    static class DefaultService {
        public String defaultMethod() {
            return null;
        }
    }
}