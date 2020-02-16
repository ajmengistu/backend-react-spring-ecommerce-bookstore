package com.ecommerce.bookstore.domain;

import java.util.stream.Stream;

import com.ecommerce.bookstore.repository.AuthorityRepository;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.security.AuthoritiesConstants;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    public CommandLineRunnerImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {
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
        userRepository.findAll().forEach(System.out::println);
    }
}