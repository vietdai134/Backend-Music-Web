package com.app.Music_Web.Infrastructure.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Music-Web API")
                        .version("1.0")
                        .description("API documentation for Music-Web application")
                        .contact(new Contact()
                                .name("vietdai134")
                                .url("yourwebsite.com")
                                .email("vietnguyentran134@email.com")));
    }
}