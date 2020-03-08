package com.ecommerce.bookstore.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecommerce.bookstore.security.jwt.TokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filters incoming requests and creates a Spring Security Principle if a header
 * corresponding to a valid user is found.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * Filter the incoming request for a valid Authorization token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = getJwtFromRequest(request);
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extract Authorization token from request header.
     * 
     * @param HttpServletRequest
     * @return Bearer JWT from Authorization Header.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.info("Extracted Authorization Bearer JWT: " + bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }

}
