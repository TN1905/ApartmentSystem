package com.poly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.client.RestTemplate;

import com.poly.dao.ApartmentDAO;
import com.poly.entity.ApartmentMap;

@SpringBootApplication
@EnableMethodSecurity
@EnableScheduling
public class AsmJava5Application {
	
	
	public static void main(String[] args) {
		SpringApplication.run(AsmJava5Application.class, args);
		
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
