package com.studyplanner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StudyPlanner Pro API")
                        .version("1.0.0")
                        .description("API REST completa para sistema de planejamento e acompanhamento de aprendizado")
                        .contact(new Contact()
                                .name("StudyPlanner Pro Team")
                                .email("contact@studyplanner.com")
                                .url("https://github.com/Joaopedromartins21/studyplanner-pro"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT obtido no endpoint de login")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
