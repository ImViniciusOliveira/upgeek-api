package com.upgeekapi;

import com.upgeekapi.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(
		//Exclui a autoconfiguração que cria o usuário e a senha padrão em memória.
		exclude = { UserDetailsServiceAutoConfiguration.class })
@EnableConfigurationProperties(JwtProperties.class)

public class UpgeekApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgeekApplication.class, args);
	}

}
