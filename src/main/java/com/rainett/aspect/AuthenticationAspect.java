package com.rainett.aspect;

import com.rainett.dto.AuthenticatedRequest;
import com.rainett.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthenticationAspect {
    private final AuthenticationService authenticationService;

    @Around("@annotation(com.rainett.annotations.Authenticated)")
    public Object authenticateRequest(ProceedingJoinPoint joinPoint)
            throws Throwable {
        AuthenticatedRequest request = findAuthenticatedRequest(joinPoint.getArgs());
        if (!authenticationService.match(request.getIdentity(), request.getPassword())) {
            throw new SecurityException("Authentication failed for user: " + request.getIdentity());
        }

        return joinPoint.proceed();
    }

    private AuthenticatedRequest findAuthenticatedRequest(Object[] methodArgs) {
        for (Object arg : methodArgs) {
            if (arg instanceof AuthenticatedRequest authenticatedRequest) {
                return authenticatedRequest;
            }
        }
        throw new IllegalArgumentException("No AuthenticatedRequest parameter found");
    }
}
