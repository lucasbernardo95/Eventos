package com.eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class EventosApplication extends SpringBootServletInitializer {//extends for add context web

	//retorna um objeto de configuração web
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(EventosApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(EventosApplication.class, args);
	}

}
