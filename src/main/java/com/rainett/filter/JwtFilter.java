package com.rainett.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rainett.dto.ErrorResponse;
import com.rainett.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer";

    private final ObjectMapper objectMapper;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String[] split = header.split(" ");
            if (split.length == 2) {
                String token = split[1];
                try {
                    authenticate(token);
                } catch (IllegalArgumentException e) {
                    sendBackResponse(e.getMessage(), response);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authenticate(String token) {
        String username = jwtUtils.validateTokenAndGetUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void sendBackResponse(String message, HttpServletResponse response)
            throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now().toString(),
                HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
