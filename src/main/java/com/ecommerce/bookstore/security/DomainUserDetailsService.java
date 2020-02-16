package com.ecommerce.bookstore.security;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.web.rest.errors.UsernameNotActivatedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String username) {
        log.debug("loadUserByUsername() {}", username);

        String lowercaseUsername = username.toLowerCase();
        return userRepository.findOneByUsername(lowercaseUsername)
                .map(user -> createSpringSecurityUser(lowercaseUsername, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseUsername + " was not activated"));
    }

    public org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseUsername,
            User user) {
        if (!user.isActivated()) {
            throw new UsernameNotActivatedException("User " + lowercaseUsername + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                grantedAuthorities);

    }
}