package com.enigma.wmb_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Warung Makan Bahari API",
        version = "1.0.0",
        description = "Warung Makan Bahari API"
))
@SecurityScheme(name = "Authorization", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT")
public class WmbApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmbApiApplication.class, args);


    }

}
