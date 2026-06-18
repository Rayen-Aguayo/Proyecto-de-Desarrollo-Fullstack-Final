package com.example.ms_medico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@ImportAutoConfiguration(exclude = {
    org.springdoc.core.configuration.SpringDocHateoasConfiguration.class
})
public class MsMedicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMedicoApplication.class, args);
	}

}
