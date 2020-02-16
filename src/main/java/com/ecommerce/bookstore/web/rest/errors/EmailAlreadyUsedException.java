package com.ecommerce.bookstore.web.rest.errors;

public class EmailAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailAlreadyUsedException(String email) {
        super("Email " + email + " already used!");
    }
}