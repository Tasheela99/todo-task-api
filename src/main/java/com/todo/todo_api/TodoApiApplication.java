package com.todo.todo_api;

import com.todo.todo_api.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class TodoApiApplication implements CommandLineRunner {

	private final UserRoleService userRoleService;

	public static void main(String[] args) {
		SpringApplication.run(TodoApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRoleService.initializeUserRoles();
	}
}
