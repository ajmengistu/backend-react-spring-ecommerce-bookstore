package com.ecommerce.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.ecommerce.bookstore.BookstoreApplication;
import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.web.rest.errors.EmailAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.errors.UsernameAlreadyUsedException;
import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration tests for {@link UserService}.
 * 
 * Source: UserServiceIT.java in Jhipster web generator.
 */
@SpringBootTest(classes = BookstoreApplication.class)
@Transactional
public class UserServiceIT {

    private static final String DEFAULT_USERNAME = "johndoe";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";

    private static final String DEFAULT_LASTNAME = "doe";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setUsername(DEFAULT_USERNAME);
        user.setPassword(UUID.randomUUID().toString());
        user.setActivated(true);
        user.setEmail(DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setCreatedBy(DEFAULT_USERNAME);
        user.setLastModifiedBy(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    public void assertThatUserMustExistToResetPassword() {
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.requestPasswordReset("invalid.Email@co.com");

        assertThat(maybeUser).isNotPresent();

        maybeUser = userService.requestPasswordReset(user.getEmail());

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getEmail()).isEqualTo(user.getEmail());
        assertThat(maybeUser.orElse(null).getEmail()).isNotNull();
        assertThat(maybeUser.orElse(null).getResetKey()).isNotNull();
    }

    @Transactional
    @Test
    public void assertThatUsernameMustNotAlreadyExistToRegister() {
        userRepository.saveAndFlush(user);

        UserVM newUser = new UserVM();
        newUser.setUsername(DEFAULT_USERNAME);

        assertThrows(UsernameAlreadyUsedException.class, () -> userService.registerUser(newUser));

        userRepository.delete(user);
    }

    @Transactional
    @Test
    public void assertThatUserEmailMustNotAlreadyExistToRegister() {
        userRepository.saveAndFlush(user);

        UserVM newUser = new UserVM();
        newUser.setUsername("newUsername");
        newUser.setEmail(DEFAULT_EMAIL);

        assertThrows(EmailAlreadyUsedException.class, () -> userService.registerUser(newUser));

        userRepository.delete(user);
    }

    @Transactional
    @Test
    public void assertThatANewUserCanRegisterAndUserIsNotActivated() {
        UserVM newUser = new UserVM();
        newUser.setUsername("NewUsername");
        newUser.setPassword("newPassword");
        newUser.setEmail("valid.newEmail@co.com");
        newUser.setFirstName("New");
        newUser.setLastName("User");

        User user = userService.registerUser(newUser);

        assertThat(user.getFirstName()).isEqualTo("New");
        assertThat(user.isActivated()).isFalse();
    }

    // Todo: updateUser(), changePassword()

    @Transactional
    @Test
    public void assertThatActivationKeyIsValid() {
        String activationKey = UUID.randomUUID().toString();
        user.setActivated(false);
        user.setActivationKey(UUID.randomUUID().toString());
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.activateRegistration(activationKey);

        assertThat(maybeUser).isNotPresent();
        assertThat(user.isActivated()).isFalse();

        userRepository.delete(user);
    }

    @Transactional
    @Test
    public void assertThatUserMustExistWithAValidActivationKey() {
        String activationKey = UUID.randomUUID().toString();
        user.setActivationKey(activationKey);
        user.setActivated(false);
        userRepository.save(user);

        Optional<User> maybeUser = userService.activateRegistration(activationKey);

        assertThat(maybeUser).isPresent();
        assertThat(user.isActivated()).isTrue();

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        user.setActivated(false);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.requestPasswordReset(user.getEmail());

        assertThat(maybeUser).isNotPresent();

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        Instant daysAgo = Instant.now().minus(25, ChronoUnit.HOURS);
        String resetKey = UUID.randomUUID().toString();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("newPassword", user.getResetKey());

        assertThat(maybeUser).isNotPresent();

        userRepository.delete(user);
    }

    @Test
    @Transactional
    public void assertThatUserCanResetPassword() {
        String oldPassword = user.getPassword();
        Instant daysAgo = Instant.now().minus(2, ChronoUnit.HOURS);
        String resetKey = UUID.randomUUID().toString();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.saveAndFlush(user);

        Optional<User> maybeUser = userService.completePasswordReset("newPassword", user.getResetKey());

        assertThat(maybeUser).isPresent();
        assertThat(maybeUser.orElse(null).getResetKey()).isNull();
        assertThat(maybeUser.orElse(null).getResetDate()).isNull();
        assertThat(maybeUser.orElse(null).getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);
    }
}