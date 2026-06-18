package com.example.ms_registro.de.atenciones.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_registro.de.atenciones.dto.ApiResponse;
import com.example.ms_registro.de.atenciones.dto.PacienteResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PacienteClient {

    private final WebClient webClient;
    private final String BASE_URL = "http://localhost:8085/api/v1/pacientes/";
    
    public PacienteResponse getPacienteClient(String run, String token){
    
    ApiResponse<PacienteResponse> response = webClient.get()
                .uri(BASE_URL + run)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<PacienteResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
        }
}
