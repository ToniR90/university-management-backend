package com.orientation.backend.shared.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Allow CORS for all paths
                        .allowedOrigins("*")    // Allow requests from any origin(just for development, restrict in production)
                        .allowedMethods("*")    // Allow GET, POST, PUT, DELETE, etc.
                        .allowedHeaders("*");   //Content-Type, Authorization, etc.
            }
        };
    }
}
