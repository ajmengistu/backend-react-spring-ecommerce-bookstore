package com.ecommerce.bookstore.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.service.MailService;
import com.ecommerce.bookstore.service.UserService;
import com.ecommerce.bookstore.web.rest.errors.EmailAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.errors.UsernameAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * {@code POST /register} : register the user.
     * 
     * @param userVM a user View Model.
     * @throws UsernameAlreadyUsedException {@code 400 (Bad Request)} if the
     *                                      username is already used.
     * @throws EmailAlreadyUsedException    {@code 400 (Bad Request)} if the email
     *                                      is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody UserVM userVM) {
        User user = userService.registerUser(userVM);
        System.out.println(user);
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key.");
        }
    }

    /**
     * {@code GET /home }
     */
    @GetMapping("/home")
    public String getHome() {
        return "Hello. This is your home.";
    }
}