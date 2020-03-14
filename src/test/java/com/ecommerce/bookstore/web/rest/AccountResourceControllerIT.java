package com.ecommerce.bookstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import com.ecommerce.bookstore.BookstoreApplication;
import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;
import com.ecommerce.bookstore.web.rest.vm.UserVM;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for {@link AccountResourceController}.
 */
@SpringBootTest(classes = BookstoreApplication.class)
@AutoConfigureMockMvc
public class AccountResourceControllerIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc accountControllerMockMvc;

    @Test
    @Transactional
    public void testRegisterValid() throws Exception {
        UserVM validUser = new UserVM();
        String username = "test-register-valid-username";
        validUser.setUsername(username);
        validUser.setPassword("password");
        validUser.setFirstName("Jefferson");
        validUser.setLastName("Test");
        validUser.setEmail("test-register-valid@example.com");

        assertThat(userRepository.findOneByUsername(username).isPresent()).isFalse();

        accountControllerMockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(validUser))).andExpect(status().isCreated());

        assertThat(userRepository.findOneByUsername(username).isPresent()).isTrue();
    }

    // TODO: duplicate username
    // TODO: duplicate email

    @Test
    @Transactional
    public void testActivateAccount() throws Exception {
        final String activationKey = "a valid activation key";
        User user = new User();
        user.setUsername("active-account");
        user.setPassword(UUID.randomUUID().toString());
        user.setActivated(false);
        user.setEmail("active-valid-email@example.com");
        user.setFirstName("John");
        user.setLastName("Arthur");
        user.setCreatedBy("active-account");
        user.setLastModifiedBy("active-account");
        user.setActivationKey(activationKey);
        userRepository.saveAndFlush(user);

        accountControllerMockMvc.perform(get("/api/activate?key={activationKey}", activationKey))
                .andExpect(status().isOk());

        user = userRepository.findOneByUsername(user.getUsername()).orElse(null);

        assertThat(user.isActivated()).isTrue();
    }

}