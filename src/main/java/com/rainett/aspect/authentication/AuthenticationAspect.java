package com.rainett.aspect.authentication;

import com.rainett.annotations.Authenticated;
import com.rainett.dto.AuthResult;
import com.rainett.exceptions.AuthenticationException;
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
        if (skipAuthentication(joinPoint)) {
            return;
        }
        HttpServletRequest currentRequest = getCurrentRequest();
        Object attribute = currentRequest.getAttribute(AUTHENTICATED_ATTRIBUTE);
        if (!(attribute instanceof AuthResult authResult)) {
            throw new AuthenticationException(500,
                    "No authentication information found in attribute " + AUTHENTICATED_ATTRIBUTE);
        }
        if (authResult.status() != AuthResult.Status.SUCCESS) {
            throw new AuthenticationException(authResult.status().getCode(), authResult.message());
        }
    }

    private static boolean skipAuthentication(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Authenticated authenticated = AnnotationUtils.findAnnotation(method, Authenticated.class);
        return authenticated != null && authenticated.ignore();
    }

    private static HttpServletRequest getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
            return servletRequestAttributes.getRequest();
        }
        throw new IllegalStateException("Aspect called outside of a request");
    }
}
