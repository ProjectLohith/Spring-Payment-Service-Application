package org.gfg.UserService;

import org.gfg.UserService.enums.UserStatus;
import org.gfg.UserService.enums.UserType;
import org.gfg.UserService.model.User;
import org.gfg.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User transactionService = User.builder()
				.phoneNo("transaction-service")
				.password(passwordEncoder.encode("transaction-service"))
				.userStatus(UserStatus.ACTIVE)
				.userType(UserType.ADMIN)
				.authorities("SERVICE").build();

		//for to maintain unique entry
		if (userRepository.findByPhoneNo("transaction-service") == null) {
			userRepository.save(transactionService);
		}

	}

}
