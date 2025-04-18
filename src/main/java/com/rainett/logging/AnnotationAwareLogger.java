package com.rainett.logging;

import java.lang.annotation.Annotation;

public interface AnnotationAwareLogger extends MethodLogger {
    Class<? extends Annotation> annotationClass();
}
