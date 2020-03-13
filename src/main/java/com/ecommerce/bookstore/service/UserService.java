package com.ecommerce.bookstore.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.ecommerce.bookstore.domain.Authority;
import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.AuthorityRepository;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.security.AuthoritiesConstants;
import com.ecommerce.bookstore.security.SecurityUtils;
import com.ecommerce.bookstore.web.rest.errors.EmailAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.errors.InvalidPasswordException;
import com.ecommerce.bookstore.web.rest.errors.UsernameAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Register a new User.
     * 
     * @param userVM A user registration information.
     * @return The registered user.
     */
    public User registerUser(UserVM userVM) {
        // username AND email must not already exist.
        userRepository.findOneByUsername(userVM.getUsername().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException(userVM.getUsername());
            }
        });
        userRepository.findOneByEmailIgnoreCase(userVM.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException(userVM.getEmail());
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(userVM.getPassword());
        newUser.setUsername(userVM.getUsername());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userVM.getFirstName());
        newUser.setLastName(userVM.getLastName());
        newUser.setEmail(userVM.getEmail());
        // new user is not active
        newUser.setActivated(false);
        // new user gets a registration key
        newUser.setActivationKey(UUID.randomUUID().toString());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        newUser.setLastModifiedBy(newUser.getUsername());
        newUser.setCreatedBy(newUser.getUsername());
        userRepository.save(newUser);
        log.debug("Created a new User: {}", newUser);
        return newUser;
    }

    /**
     * Remove a non-active user.
     * 
     * @param existingUser
     * @return True, if the user is deleted. False, if the user is already
     *         activated.
     */
    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }

    /**
     * Activate the newly registered user.
     * 
     * @param key Activation code/key emailed to the newly registered user.
     * @return An Optional
     */
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key).map(user -> {
            // activate given user if activation key is valid.
            user.setActivated(true);
            user.setActivationKey(null);
            log.debug("Activated user: {}", user);
            return user;
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    /**
     * Update basic information (firstName, lastName, email) for the current user.
     * 
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     */
    public void updateUser(String firstName, String lastName, String email) {
        SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneByUsername).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            log.debug("Changed Information for User: {}", user);
        });
    }

    /**
     * Update the current user's existing password.
     * 
     * @param currentNotEncryptedPassword
     * @param newPassword
     */
    public void changePassword(String currentNotEncryptedPassword, String newPassword) {
        SecurityUtils.getCurrentUsername().flatMap(userRepository::findOneByUsername).ifPresent(user -> {
            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(currentNotEncryptedPassword, currentEncryptedPassword)) {
                throw new InvalidPasswordException("Current password does not match existing password!");
            }
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }

    /**
     * Assign the user with a password reset key/token and token expiration date.
     * 
     * @param email
     * @return The user that requested password reset with a password reset token
     *         and expiration date.
     */
    public Optional<User> requestPasswordReset(String email) {
        return userRepository.findOneByEmailIgnoreCase(email).filter(User::isActivated).map(user -> {
            user.setResetKey(UUID.randomUUID().toString());
            user.setResetDate(Instant.now());
            return user;
        });
    }

    /**
     * Complete the password reset request by verifying that the reset key/token and
     * key/token expiration date are both valid.
     */
    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400))).map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    return user;
                });
    }
}