package com.example.ludogorieSoft.village;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={ DataSourceAutoConfiguration.class})
public class VillageApplication {

	public static void main(String[] args) {
		SpringApplication.run(VillageApplication.class, args);
	}

}
