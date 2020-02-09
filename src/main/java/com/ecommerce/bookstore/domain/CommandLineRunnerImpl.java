package com.ecommerce.bookstore.domain;

import java.util.stream.Stream;

import com.ecommerce.bookstore.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final UserRepository userRepository;

    public CommandLineRunnerImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
	public void run(String... strings) throws Exception {
		Stream.of("John", "Tomas", "Albert", "Franklin", "Clifford")
		.forEach(x -> userRepository.save(new User(x)));

		userRepository.findAll().forEach(System.out::println);
	}
}