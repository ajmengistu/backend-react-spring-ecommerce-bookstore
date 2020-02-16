package com.ecommerce.bookstore.web.rest;

import javax.validation.Valid;

import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.service.UserService;
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

    private final UserRepository userRepository;

    private final UserService userService;

    public AccountResource(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * {@code POST /register} : register the user.
     * 
     * @param userVM a user View Model.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserVM userVM) {
        System.out.println(userVM);
        User user = userService.registerUser(userVM);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("this was a bad reequest");
    }
}