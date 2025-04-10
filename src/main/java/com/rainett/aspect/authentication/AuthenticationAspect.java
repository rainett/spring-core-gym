package com.rainett.aspect.authentication;

import com.rainett.annotations.Authenticated;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthenticationAspect {
    private static final String AUTHENTICATED_ATTRIBUTE = "isAuthenticated";

    @Before("within(@com.rainett.annotations.Authenticated *) || @annotation(com.rainett.annotations.Authenticated)")
    public void checkAuthentication(JoinPoint joinPoint) {
        Authenticated annotation = getAnnotation(joinPoint);
        if (annotation != null && annotation.ignore()) {
            return;
        }
        HttpServletRequest currentRequest = getCurrentRequest();
        Object attribute = currentRequest.getAttribute(AUTHENTICATED_ATTRIBUTE);
        if (!(attribute instanceof IllegalArgumentException)) {
            return;
        }
        throw (IllegalArgumentException) attribute;
    }

    private static Authenticated getAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return AnnotationUtils.findAnnotation(method, Authenticated.class);
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        throw new IllegalStateException("Aspect called outside of a request");
    }
}
