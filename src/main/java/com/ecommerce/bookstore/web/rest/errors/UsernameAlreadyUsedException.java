package com.ecommerce.bookstore.web.rest.errors;

public class UsernameAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsernameAlreadyUsedException(String username) {
        super("Username " + username + " already used!");
    }
}