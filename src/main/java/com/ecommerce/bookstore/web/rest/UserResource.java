package com.ecommerce.bookstore.web.rest;

import java.util.List;

import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing existing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * {@code GET /users} : get all the users.
     * 
     * @return a list of all active and non-active users.
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}