package com.example.ludogorieSoft.village;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "Village API", description = "Documentation of Village API"))
public class VillageApplication {

	public static void main(String[] args) {
		SpringApplication.run(VillageApplication.class, args);
	}

}