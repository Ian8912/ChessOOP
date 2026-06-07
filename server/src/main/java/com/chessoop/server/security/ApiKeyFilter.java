package com.chessoop.server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Requires a shared-secret API key on mutating requests to {@code /api/v1/results}.
 *
 * <p>Clients must send the configured key in the {@code X-API-Key} header. The key
 * is read from {@code app.api-key} (env {@code APP_API_KEY}). When that value is
 * blank the guard is disabled — convenient for local development — and a warning
 * is logged at startup so an unprotected deployment is obvious. Read endpoints
 * (e.g. the leaderboard) are intentionally left open.</p>
 */
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiKeyFilter.class);
    private static final String HEADER = "X-API-Key";
    private static final String PROTECTED_PATH = "/api/v1/results";

    private final String apiKey;

    public ApiKeyFilter(@Value("${app.api-key:}") String apiKey) {
        this.apiKey = apiKey;
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("app.api-key is not set — write endpoints are UNPROTECTED. "
                + "Set APP_API_KEY in any deployed environment.");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (requiresKey(request) && !keyMatches(request)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write("{\"error\":\"invalid or missing API key\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    /** Only write requests to the results endpoint are guarded. */
    private boolean requiresKey(HttpServletRequest request) {
        return HttpMethod.POST.matches(request.getMethod())
            && request.getRequestURI().startsWith(PROTECTED_PATH);
    }

    /** True if the guard is disabled (blank key) or the request supplies the correct key. */
    private boolean keyMatches(HttpServletRequest request) {
        if (apiKey == null || apiKey.isBlank()) {
            return true; // guard disabled in dev
        }
        return apiKey.equals(request.getHeader(HEADER));
    }
}
