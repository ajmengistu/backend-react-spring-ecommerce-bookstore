package com.ecommerce.bookstore.domain;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import com.ecommerce.bookstore.repository.AuthorityRepository;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.security.AuthoritiesConstants;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    public CommandLineRunnerImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        // Stream.of("SpaceX", "Blue Origin", "Tesla Motors", "Thomas Jefferson",
        // "Einstein", "Asimov", "Franklin",
        // "Sagan").forEach(x -> userRepository.save(new User(x)));
        Stream.of(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER, AuthoritiesConstants.ANONYMOUS)
                .forEach(auth -> {
                    Authority a = new Authority();
                    a.setName(auth);
                    authorityRepository.save(a);
                });

        // User newUser = new User();
        // String encryptedPassword = passwordEncoder.encode("Fiji");
        // newUser.setUsername("Fiji");
        // newUser.setPassword(encryptedPassword);
        // newUser.setFirstName("Fiji");
        // newUser.setLastName("Fiji");
        // newUser.setEmail("fiji@co.com");
        // // new user is not active
        // newUser.setActivated(true);
        // // new user gets a registration key
        // newUser.setActivationKey(UUID.randomUUID().toString());
        // Set<Authority> authorities = new HashSet<>();
        // authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        // newUser.setAuthorities(authorities);
        // newUser.setLastModifiedBy(newUser.getUsername());
        // newUser.setCreatedBy(newUser.getUsername());
        // userRepository.save(newUser);

        userRepository.findAll().forEach(System.out::println);
    }
}