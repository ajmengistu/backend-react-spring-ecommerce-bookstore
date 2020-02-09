package com.ecommerce.bookstore.repository;

import java.util.List;

import com.ecommerce.bookstore.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
    public List<User> findAll();    
}