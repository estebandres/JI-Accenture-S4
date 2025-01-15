package com.mindhub.todolist;

import com.mindhub.todolist.models.AppUser;
import com.mindhub.todolist.models.Task;
import com.mindhub.todolist.models.TaskStatus;
import com.mindhub.todolist.models.UserRole;
import com.mindhub.todolist.repositories.AppUserRepository;
import com.mindhub.todolist.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TodolistApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(@Value("${jwt.secret}") String secretKey,  AppUserRepository appUserRepository, TaskRepository taskRepository) {
		return args -> {
			System.out.println("This is the secret key: " + secretKey);
			AppUser firstUser = new AppUser("Steven", passwordEncoder.encode("Steve123456789!"), "steven@gmail.com");
			appUserRepository.save(firstUser);
			System.out.println(firstUser);

			Task taskOne = new Task("comprar carne","vacío 1kg", TaskStatus.PENDING);
			taskRepository.save(taskOne);
			System.out.println(taskOne);

			firstUser.addTask(taskOne);
			System.out.println(firstUser);

			Task taskTwo = new Task("comprar verdura","Papa 2kg", TaskStatus.PENDING);
			Task taskThree = new Task("comprar bebida","Fanta 3L", TaskStatus.COMPLETED);

			taskRepository.save(taskOne);
			firstUser.addTask(taskTwo);
			firstUser.addTask(taskThree);
			taskRepository.save(taskThree);
			taskRepository.save(taskTwo);

			AppUser secondUser = new AppUser("Carlito", passwordEncoder.encode("Carlito123456789!"), "charles@gmail.com");
			secondUser.setRole(UserRole.ADMIN);
			System.out.println(secondUser);
			appUserRepository.save(secondUser);

			Task taskFour = new Task("comprar flores","Rosas 4", TaskStatus.PENDING);
			taskRepository.save(taskFour);

		};
	}
}
