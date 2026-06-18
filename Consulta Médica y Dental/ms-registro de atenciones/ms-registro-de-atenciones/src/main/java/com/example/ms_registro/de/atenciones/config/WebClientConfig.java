package com.example.ms_registro.de.atenciones.config;

import org.springframework.context.annotation.*;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}