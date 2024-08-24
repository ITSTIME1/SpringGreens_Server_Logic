package com.spring_greens.logic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@EnableScheduling
@SpringBootApplication
public class LogicApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogicApplication.class, args);
	}
}
