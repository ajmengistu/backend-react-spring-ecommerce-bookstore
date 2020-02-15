package com.ecommerce.bookstore.web.rest;

import javax.validation.Valid;

import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
     * {@code POST /register} : register the user.
     * 
     * @param userVM a user View Model.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserVM userVM) {
        System.out.println(userVM.toString());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("this was a bad reequest");
    }
}