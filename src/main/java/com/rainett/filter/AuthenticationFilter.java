package com.rainett.filter;

import com.rainett.dto.AuthResult;
import com.rainett.exceptions.LoginException;
import com.rainett.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_ATTRIBUTE = "isAuthenticated";
    private static final String BASIC_PREFIX = "Basic ";

    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        AuthResult authResult = getAuthResult(header);
        request.setAttribute(AUTHORIZATION_ATTRIBUTE, authResult);
        filterChain.doFilter(request, response);
    }

    private AuthResult getAuthResult(String header) {
        AuthResult authResult;
        if (header == null || header.isEmpty()) {
            authResult = new AuthResult(AuthResult.Status.INVALID_HEADER,
                    "Authorization header not found");
        } else if (!header.startsWith(BASIC_PREFIX)) {
            authResult = new AuthResult(AuthResult.Status.INVALID_HEADER,
                    "Authorization header must start with '" + BASIC_PREFIX + "'");
        } else {
            authResult = authenticate(header);
        }
        return authResult;
    }

    private AuthResult authenticate(String header) {
        String[] credentials;
        try {
            credentials = decodeCredentialsFromHeader(header);
        } catch (IllegalArgumentException ex) {
            return new AuthResult(AuthResult.Status.INVALID_HEADER, ex.getMessage());
        }
        String username = credentials[0];
        String password = credentials[1];
        return authenticate(username, password);
    }

    private static String[] decodeCredentialsFromHeader(String header) {
        String base64Credentials = header.substring(BASIC_PREFIX.length()).trim();
        byte[] decodedCredentials;
        try {
            decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid Base64 authorization header encoding");
        }
        String[] split = new String(decodedCredentials).split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid authorization header content, " +
                                               "expected username:password");
        }
        return split;
    }

    private AuthResult authenticate(String username, String password) {
        try {
            authenticationService.authenticate(username, password);
        } catch (LoginException ex) {
            return new AuthResult(AuthResult.Status.UNAUTHORIZED, ex.getMessage());
        }
        return new AuthResult(AuthResult.Status.SUCCESS, null);
    }
}
