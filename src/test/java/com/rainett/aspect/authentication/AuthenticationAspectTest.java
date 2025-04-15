package com.rainett.aspect.authentication;

import static org.junit.jupiter.api.Assertions.*;

import com.rainett.annotations.Authenticated;
import com.rainett.dto.AuthResult;
import com.rainett.exceptions.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootTest
@Import(AuthenticationAspectTest.TestConfig.class)
class AuthenticationAspectTest {
    private static final String AUTHENTICATED_ATTRIBUTE = "isAuthenticated";

    @Autowired
    private DummyService dummyService;

    @BeforeEach
    void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    void cleanup() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    @DisplayName("Aspect passes authenticated requests")
    void testAuthenticatedSuccess() {
        HttpServletRequest request = getRequest();
        AuthResult authResult = new AuthResult(AuthResult.Status.SUCCESS, "Authenticated");
        request.setAttribute(AUTHENTICATED_ATTRIBUTE, authResult);
        assertDoesNotThrow(() -> dummyService.securedMethod());
    }

    @Test
    @DisplayName("Aspect throws exception for bad requests")
    void testAuthenticatedFailure() {
        HttpServletRequest request = getRequest();
        String message = "Header is invalid";
        AuthResult.Status status = AuthResult.Status.INVALID_HEADER;
        AuthResult authResult = new AuthResult(status, message);
        request.setAttribute("isAuthenticated", authResult);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> dummyService.securedMethod());
        assertEquals(status.getCode(), exception.getCode());
        assertEquals(message, exception.getMessage());
    }

    @Test
    @DisplayName("Aspect skips unsecured methods")
    void testSkipAuthentication() {
        assertDoesNotThrow(() -> dummyService.methodIgnored());
    }

    @Test
    @DisplayName("Aspect throws exception when no authentication information is found")
    void testNoAuthenticationInformation() {
        HttpServletRequest request = getRequest();
        request.setAttribute(AUTHENTICATED_ATTRIBUTE, null);
        assertThrows(AuthenticationException.class, () -> dummyService.securedMethod());
    }

    @Test
    @DisplayName("Aspect throws exception when used in non-request context")
    void testNonRequestContext() {
        RequestContextHolder.setRequestAttributes(null);
        assertThrows(IllegalStateException.class, () -> dummyService.securedMethod());
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        return requestAttributes.getRequest();
    }

    @Configuration
    @EnableAspectJAutoProxy
    @Import({AuthenticationAspect.class, DummyService.class})
    static class TestConfig {
    }

    @Authenticated
    static class DummyService {
        public void securedMethod() {
            // Secured method
        }

        @Authenticated(ignore = true)
        public void methodIgnored() {
            // Unsecured method
        }
    }
}