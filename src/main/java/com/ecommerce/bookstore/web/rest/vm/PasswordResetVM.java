package com.ecommerce.bookstore.web.rest.vm;

/**
 * View Model object for storing a user's email and client origin.
 */
public class PasswordResetVM {

    private String email;

    private String clientOrigin;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClientOrigin() {
        return clientOrigin;
    }

    public void setClientOrigin(String clientOrigin) {
        this.clientOrigin = clientOrigin;
    }

    @Override
    public String toString() {
        return "PasswordResetVM [clientOrigin=" + clientOrigin + ", email=" + email + "]";
    }
}