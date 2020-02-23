package com.ecommerce.bookstore.web.rest.errors;

/**
 * An exception thrown by AccountResource when a user password is invalid.
 */
public class InvalidPasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidPasswordException(String msg) {
        super(msg);
    }
}