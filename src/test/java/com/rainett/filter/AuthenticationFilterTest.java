package com.rainett.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.rainett.dto.AuthResult;
import com.rainett.exceptions.LoginException;
import com.rainett.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterTest {
    private static final String AUTHORIZATION_ATTRIBUTE = "isAuthenticated";

    @InjectMocks
    private AuthenticationFilter filter;

    @Mock
    private AuthenticationService authenticationService;

    static Stream<Arguments> invalidHeaderProvider() {
        String invalidCredentials =
                Base64.getEncoder().encodeToString("invalid".getBytes(StandardCharsets.UTF_8));
        return Stream.of(
                Arguments.of(null, "Authorization header not found"),
                Arguments.of("", "Authorization header not found"),
                Arguments.of("Bearer token", "Authorization header must start with " +
                                             "'Basic '"),
                Arguments.of("Basic token-invalid", "Invalid Base64 authorization " +
                                                    "header encoding"),
                Arguments.of("Basic " + invalidCredentials,
                        "Invalid authorization header content, " +
                        "expected username:password")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidHeaderProvider")
    @DisplayName("Handles invalid auth header")
    void testInvalidAuthHeader(String header, String expectedMessage)
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        if (header != null) {
            request.addHeader("Authorization", header);
        }
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();
        filter.doFilterInternal(request, response, chain);
        AuthResult authResult = (AuthResult) request.getAttribute(AUTHORIZATION_ATTRIBUTE);
        assertNotNull(authResult);
        assertEquals(authResult.message(), expectedMessage);
    }

    @Test
    @DisplayName("Handles authentication failed")
    void testAuthenticationFailed() throws ServletException, IOException {
        String credentials = Base64.getEncoder()
                .encodeToString("user:pass".getBytes(StandardCharsets.UTF_8));
        String header = "Basic " + credentials;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", header);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = Mockito.spy(new MockFilterChain());
        doThrow(new LoginException()).when(authenticationService)
                .authenticate(anyString(), anyString());

        filter.doFilterInternal(request, response, chain);

        verify(authenticationService, times(1))
                .authenticate(anyString(), anyString());
        verify(chain, times(1)).doFilter(request, response);
        AuthResult authResult = (AuthResult) request.getAttribute(AUTHORIZATION_ATTRIBUTE);
        assertNotNull(authResult);
        assertEquals(AuthResult.Status.UNAUTHORIZED, authResult.status());
    }

    @Test
    @DisplayName("Overall flow test")
    void testDoFilterInternal_overallFlowSuccessful() throws ServletException, IOException {
        String credentials = Base64.getEncoder()
                .encodeToString("user:pass".getBytes(StandardCharsets.UTF_8));
        String header = "Basic " + credentials;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", header);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = Mockito.spy(new MockFilterChain());

        filter.doFilterInternal(request, response, chain);

        verify(authenticationService, times(1))
                .authenticate("user", "pass");
        verify(chain, times(1)).doFilter(request, response);
        AuthResult authResult = (AuthResult) request.getAttribute(AUTHORIZATION_ATTRIBUTE);
        assertNotNull(authResult);
        assertEquals(AuthResult.Status.SUCCESS, authResult.status());
    }
}