package com.example.task_list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TaskListApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskListApplication.class, args);
	}

}
