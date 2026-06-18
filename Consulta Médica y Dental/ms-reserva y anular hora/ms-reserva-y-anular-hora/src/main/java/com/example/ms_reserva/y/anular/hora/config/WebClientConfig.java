package com.example.ms_reserva.y.anular.hora.config;

import org.springframework.context.annotation.*;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}