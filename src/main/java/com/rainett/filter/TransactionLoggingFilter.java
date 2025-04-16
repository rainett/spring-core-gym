package com.rainett.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TransactionLoggingFilter extends OncePerRequestFilter {
    public static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";
    public static final String TRANSACTION_ID_MDC = "transactionId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String transactionId = getTransactionId(request);
        MDC.put(TRANSACTION_ID_MDC, transactionId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRANSACTION_ID_MDC);
        }
    }

    private static String getTransactionId(HttpServletRequest request) {
        String transactionId = request.getHeader(TRANSACTION_ID_HEADER);
        if (transactionId == null) {
            transactionId = UUID.randomUUID().toString();
        }
        return transactionId;
    }
}
