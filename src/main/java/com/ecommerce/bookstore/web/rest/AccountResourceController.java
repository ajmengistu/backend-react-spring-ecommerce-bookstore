package com.ecommerce.bookstore.web.rest;

import java.util.Optional;

import javax.validation.Valid;

import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.security.SecurityUtils;
import com.ecommerce.bookstore.service.MailService;
import com.ecommerce.bookstore.service.UserService;
import com.ecommerce.bookstore.service.dto.PasswordChangeDTO;
import com.ecommerce.bookstore.service.dto.UserDTO;
import com.ecommerce.bookstore.web.rest.errors.EmailAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.errors.EmailNotFoundException;
import com.ecommerce.bookstore.web.rest.errors.InvalidPasswordException;
import com.ecommerce.bookstore.web.rest.errors.UsernameAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.vm.KeyAndPasswordVM;
import com.ecommerce.bookstore.web.rest.vm.PasswordResetVM;
import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing the current user's account.
 * 
 * Source: Jhipster.tech (React + Spring).
 */
@RestController
@RequestMapping("/api")
public class AccountResourceController {

    private static class AccountResourceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(AccountResourceController.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResourceController(UserRepository userRepository, UserService userService, MailService mailService) {
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
        log.debug("Account registration requested by, {}", userVM);
        User user = userService.registerUser(userVM);
        mailService.sendActivationEmail(user, userVM);
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
     * {@code GET /account} : get the current user.
     * 
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user
     *                          couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities().map(UserDTO::new)
                .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST /account} : update the current user information (firstName,
     * lastName, and Email).
     * 
     * @param userDto the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is
     *                                   already used.
     * @throws RuntimeException          {@code 500 (Internal Server Error)} if the
     *                                   user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDto) {
        String currentUserUsername = SecurityUtils.getCurrentUsername() // Make sure the user is signed in.
                .orElseThrow(() -> new AccountResourceException("Current user username is not found."));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDto.getEmail());
        // Make sure the NEW email does not already belong to the current user.
        if (existingUser.isPresent() && (!existingUser.get().getUsername().equalsIgnoreCase(currentUserUsername))) {
            throw new EmailAlreadyUsedException("There is already an email associated with that username!");
        }
        Optional<User> user = userRepository.findOneByUsername(currentUserUsername);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());
    }

    /**
     * {@code POST /account/change-password} : changes the current user's password.
     * 
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new
     *                                  password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException("New password is invalid.");
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST /account/reset-password/init} : Send an email to reset the
     * password of the user.
     * <p>
     * If the user forgets their password and want to reset it.
     * 
     * @param the mail of the user.
     * @throws EmailNotFoundException {@code 400 (Bad Request)} if the email address
     *                                is not registered.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody PasswordResetVM passwordResetVm) {
        mailService.sendPasswordResetMail(
                userService.requestPasswordReset(passwordResetVm.getEmail())
                        .orElseThrow(() -> new EmailNotFoundException("Email address is not registered.")),
                passwordResetVm.getClientOrigin());
    }

    /**
     * {@code POST /account/reset-password/finish} : Finish to reset the password of
     * the user.
     * 
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is
     *                                  incorrect.
     * @throws RuntimeException         {@code 500 (Internal Server Error)} if the
     *                                  password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPasswordVm) {
        if (!checkPasswordLength(keyAndPasswordVm.getNewPassword())) {
            throw new InvalidPasswordException("Password is invalid.");
        }
        Optional<User> user = userService.completePasswordReset(keyAndPasswordVm.getNewPassword(),
                keyAndPasswordVm.getKey());
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= UserVM.PASSWORD_MIN_LENGTH
                && password.length() <= UserVM.PASSWORD_MAX_LENGTH;
    }
}
