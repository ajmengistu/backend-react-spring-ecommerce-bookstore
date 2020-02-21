package com.ecommerce.bookstore.repository;

import java.util.List;
import java.util.Optional;

import com.ecommerce.bookstore.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    Optional<User> findOneByUsername(String username);

    Optional<User> findOneByEmailIgnoreCase(String email);

	Optional<User> findOneByActivationKey(String key);
}