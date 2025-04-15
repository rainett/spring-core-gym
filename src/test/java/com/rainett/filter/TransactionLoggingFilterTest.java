package com.rainett.filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TransactionLoggingFilterTest {
    private static final String TRANSACTION_ID_MDC = "transactionId";

    private final TransactionLoggingFilter filter = new TransactionLoggingFilter();

    @Test
    @DisplayName("General flow test")
    void generalFlowTest() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AtomicReference<String> mdcValueDuringFilter = new AtomicReference<>();
        FilterChain filterChain = getFilterChain(mdcValueDuringFilter);

        assertNull(MDC.get(TRANSACTION_ID_MDC));
        filter.doFilterInternal(request, response, filterChain);
        assertNull(MDC.get(TRANSACTION_ID_MDC));
        assertNotNull(mdcValueDuringFilter.get(), "Expected to generate a transactionId");
        verify(filterChain, times(1)).doFilter(request, response);
    }

    private static FilterChain getFilterChain(AtomicReference<String> mdcValueDuringFilter) {
        FilterChain filterChain = new MockFilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response)
                    throws IOException, ServletException {
                mdcValueDuringFilter.set(MDC.get(TRANSACTION_ID_MDC));
                super.doFilter(request, response);
            }
        };
        filterChain = spy(filterChain);
        return filterChain;
    }
}