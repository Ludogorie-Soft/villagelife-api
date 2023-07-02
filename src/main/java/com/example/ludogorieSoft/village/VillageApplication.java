package com.example.ludogorieSoft.village;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class VillageApplication {

	public static void main(String[] args) {
		SpringApplication.run(VillageApplication.class, args);
	}

}
