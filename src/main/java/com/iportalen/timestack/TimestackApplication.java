package com.iportalen.timestack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import freemarker.template.Configuration;

@SpringBootApplication
public class TimestackApplication {
	
	@Autowired
	protected Configuration freemarkerConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(TimestackApplication.class, args);
	}
}
