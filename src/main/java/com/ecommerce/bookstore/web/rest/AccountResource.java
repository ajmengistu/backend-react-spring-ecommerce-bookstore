package com.ecommerce.bookstore.web.rest;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    public AccountResource() {
    }

    /**
     * Register the user.
     */
    @RequestMapping("/register")
    public ResponseEntity registerAccount(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("this was a bad reequest");
    }
}