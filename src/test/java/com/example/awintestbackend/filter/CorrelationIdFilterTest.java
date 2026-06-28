package com.example.awintestbackend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CorrelationIdFilterTest {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private CorrelationIdFilter correlationIdFilter;

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @Test
    void doFilter_WithCorrelationIdHeader_ShouldUseProvidedId() throws ServletException, IOException {
        String existingId = UUID.randomUUID().toString();
        when(request.getHeader(CORRELATION_ID_HEADER)).thenReturn(existingId);

        correlationIdFilter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader(CORRELATION_ID_HEADER, existingId);
        verify(filterChain).doFilter(request, response);
        assertNull(MDC.get(CORRELATION_ID_MDC_KEY), "MDC should be cleared after filter");
    }

    @Test
    void doFilter_WithoutCorrelationIdHeader_ShouldGenerateNewId() throws ServletException, IOException {
        when(request.getHeader(CORRELATION_ID_HEADER)).thenReturn(null);

        correlationIdFilter.doFilterInternal(request, response, filterChain);

        verify(response).setHeader(eq(CORRELATION_ID_HEADER), anyString());
        verify(filterChain).doFilter(request, response);
        assertNull(MDC.get(CORRELATION_ID_MDC_KEY), "MDC should be cleared after filter");
    }

    @Test
    void doFilter_ShouldSetMdcDuringChainExecution() throws ServletException, IOException {
        String existingId = UUID.randomUUID().toString();
        when(request.getHeader(CORRELATION_ID_HEADER)).thenReturn(existingId);

        doAnswer(_ -> {
            assertEquals(existingId, MDC.get(CORRELATION_ID_MDC_KEY));
            return null;
        }).when(filterChain).doFilter(request, response);

        correlationIdFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilter_ShouldClearMdcEvenOnException() throws ServletException, IOException {
        when(request.getHeader(CORRELATION_ID_HEADER)).thenReturn("some-id");
        doThrow(new RuntimeException("Test exception")).when(filterChain).doFilter(request, response);

        assertThrows(RuntimeException.class, () ->
                correlationIdFilter.doFilterInternal(request, response, filterChain)
        );

        assertNull(MDC.get(CORRELATION_ID_MDC_KEY), "MDC should be cleared even on exception");
    }
}
