package com.example.paytm.inpg;

import com.example.paytm.inpg.entities.User;
import com.example.paytm.inpg.services.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class, args);

		//UserRepository userRepository = context.getBean(UserRepository.class);

		//List<User> users = userRepository.findByEmailid("eee");

		//users.forEach(e->System.out.println(e.getFirstname()));
	}
}
