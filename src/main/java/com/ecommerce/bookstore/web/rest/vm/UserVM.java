package com.ecommerce.bookstore.web.rest.vm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * A View Model for a User.
 */
public class UserVM {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @NotBlank
    @Size(min = 2, max = 50)
    private String username;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(min = 5, max = 100)
    private String email;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    // The host/origin address of where the frontend/client app is hosted on.
    // Needed for sending email to the client/user.
    private String clientOrigin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClientOrigin() {
        return clientOrigin;
    }

    public void setClientOrigin(String clientOrigin) {
        this.clientOrigin = clientOrigin;
    }

    @Override
    public String toString() {
        return "UserVM [clientOrigin=" + clientOrigin + ", email=" + email + ", firstName=" + firstName + ", lastName="
                + lastName + ", password=" + password + ", username=" + username + "]";
    }

}