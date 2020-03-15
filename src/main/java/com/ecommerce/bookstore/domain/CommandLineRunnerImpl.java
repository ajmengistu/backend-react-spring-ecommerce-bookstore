package com.ecommerce.bookstore.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.ecommerce.bookstore.repository.AuthorityRepository;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.security.AuthoritiesConstants;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;

    public CommandLineRunnerImpl(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    private List<User> createAUser() {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        List<User> users = new ArrayList<>();

        User user = new User();
        user.setUsername("pierre");
        user.setPassword("timeless");
        user.setActivated(true);
        user.setEmail("user@example.com");
        user.setFirstName("Pierre");
        user.setLastName("Bismarck");
        user.setCreatedBy("pierre");
        user.setLastModifiedBy("pierre");
        user.setAuthorities(authorities);
        users.add(user);

        // multiple cursors in vim in vscode. ctrl + alt + up or down
        User user2 = new User();
        user2.setUsername("pierre2");
        user2.setPassword("timeless");
        user2.setActivated(true);
        user2.setEmail("user2@example.com");
        user2.setFirstName("Pierre");
        user2.setLastName("Bismarck");
        user2.setCreatedBy("pierre2");
        user2.setLastModifiedBy("pierre2");
        user.setAuthorities(authorities);
        users.add(user2);

        return users;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of(AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER, AuthoritiesConstants.ANONYMOUS)
                .forEach(auth -> {
                    Authority a = new Authority();
                    a.setName(auth);
                    authorityRepository.save(a);
                });
        createAUser().stream().forEach(user -> {
            userRepository.save(user);
        });
    }
}