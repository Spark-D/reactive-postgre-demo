package com.example.reactivepostgredemo;

import com.example.reactivepostgredemo.model.Todo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;

@SpringBootApplication
public class ReactivePostgreDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactivePostgreDemoApplication.class, args);
	}

}
