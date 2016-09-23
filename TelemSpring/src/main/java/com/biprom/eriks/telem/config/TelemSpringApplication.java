package com.biprom.eriks.telem.config;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableVaadin
@ComponentScan("com.biprom.eriks.telem")
public class TelemSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemSpringApplication.class, args);
	}
}
