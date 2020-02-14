package com.ecommerce.bookstore;

import java.util.List;

import com.ecommerce.bookstore.domain.User;
import com.ecommerce.bookstore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:3000", "https://frontend-bookstore-react-sp.herokuapp.com", "https://frontend-bookstore-react-spring.netlify.com"})
@RestController
@SpringBootApplication
public class BookstoreApplication {
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@GetMapping("/api")
	public List<User> getHome() {
		return userRepository.findAll();
	}
}
