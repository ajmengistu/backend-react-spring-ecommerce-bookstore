package com.ecommerce.bookstore.web.rest.errors;

import org.springframework.security.core.AuthenticationException;

/**
 * An exception thrown by Authentication service when a user is not activated.
 */
public class UsernameNotActivatedException extends AuthenticationException {

    public static final long serialVersionUID = 1L;

    public UsernameNotActivatedException(String msg) {
        super(msg);
    }
}