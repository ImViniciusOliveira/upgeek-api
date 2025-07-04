package com.upgeekapi;

import com.upgeekapi.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class UpgeekApplication {

	public static void main(String[] args) {
		SpringApplication.run(UpgeekApplication.class, args);
	}

}
