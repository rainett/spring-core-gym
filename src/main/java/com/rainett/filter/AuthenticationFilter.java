package com.rainett.filter;

import com.rainett.exceptions.LoginException;
import com.rainett.service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
        IllegalArgumentException errorResponse = tryAuthenticate(request);
        request.setAttribute(AUTHORIZATION_ATTRIBUTE, errorResponse);
        filterChain.doFilter(request, response);
    }

    private IllegalArgumentException tryAuthenticate(HttpServletRequest request) {
        try {
            authenticate(request);
        } catch (IllegalArgumentException ex) {
            return ex;
        }
        return null;
    }

    private void authenticate(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BASIC_PREFIX)) {
            throw new IllegalArgumentException("Authorization header not found or invalid, " +
                                               "expected " + BASIC_PREFIX);
        }
        String[] credentials = decodeCredentialsFromHeader(header);
        String username = credentials[0];
        String password = credentials[1];
        authenticate(username, password);
    }

    private static String[] decodeCredentialsFromHeader(String header) {
        String base64Credentials = header.substring(BASIC_PREFIX.length()).trim();
        byte[] decodedCredentials = Base64.getDecoder().decode(base64Credentials);
        String[] split = new String(decodedCredentials).split(":");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid authorization header content, " +
                                               "expected username:password");
        }
        return split;
    }

    private void authenticate(String username, String password) {
        try {
            authenticationService.authenticate(username, password);
        } catch (LoginException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
