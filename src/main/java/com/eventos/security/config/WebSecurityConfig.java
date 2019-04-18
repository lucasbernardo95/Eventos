package com.eventos.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // indica ao spring que essa é uma classe de configuração
@EnableWebSecurity // habilita que esta classe vai configurar o websecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImplements userDetailsService;
	
	// Define as URLs que precisam de autenticação para ser acessadas ou não
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/evento").permitAll() 
			.anyRequest().authenticated() 
			.antMatchers("/eventos/index.xhtml").authenticated()//o que estiver dentro /eventos/ precisa estar autenticado
			.and().formLogin().permitAll() //define o formulário padrão do security e se tiver autenticado redireciona para /eventos/index.xhtml
			.defaultSuccessUrl("/eventos/index.xhtml")
	        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));// /logout encerra a cessão
	}

	// define como vai ser feita a autenticação
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	// serve para que o spring security não bloqueie as páginas estáticas como
	// styles.css e etc
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}