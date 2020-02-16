package com.ecommerce.bookstore.web.rest.errors; 
import java.net.URI;

public final class ErrorConstants {

    public static final String PROBLEM_BASE_URI = "https://localhost:8082/problem";
    public static final URI USERNAME_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URI + "/username-already-used");

    private ErrorConstants() {
    }
}