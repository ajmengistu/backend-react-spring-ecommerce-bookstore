package com.ecommerce.bookstore.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ecommerce.bookstore.domain.Authority;
import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.AuthorityRepository;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.security.AuthoritiesConstants;
import com.ecommerce.bookstore.web.rest.errors.EmailAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.errors.UsernameAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }
}