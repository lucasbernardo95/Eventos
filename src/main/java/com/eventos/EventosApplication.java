package com.eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EventosApplication extends SpringBootServletInitializer {//extends for add context web

	//retorna um objeto de configuração web
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EventosApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(EventosApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("admin"));
	}

}
