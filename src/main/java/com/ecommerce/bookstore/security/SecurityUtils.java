package com.ecommerce.bookstore.security;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for Spring Security.
 * 
 * Source: Jhipster (React + Spring). SecurityUtils.java
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the username of the current user.
     * 
     * @return the username of the current user.
     */
    public static Optional<String> getCurrentUsername() {
       // @formatter:off 
       SecurityContext securityContext = SecurityContextHolder.getContext();
       return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    return (String) authentication.getPrincipal();
                }
                return null;
            });
    }

    /**
     * Get the JWT of the current user.
     * 
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        // @formatter:off
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     * 
     * @return true if the user is authenticated false otherwise.
     */
    public static boolean isAuthenticated() {
        // @formatter:off
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
            .orElse(false);
    }

    /**
     * Check if the current user has a specific authority (security role).
     * <p> 
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     * 
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        // @formatter:off
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
            .orElse(false);
    }
}