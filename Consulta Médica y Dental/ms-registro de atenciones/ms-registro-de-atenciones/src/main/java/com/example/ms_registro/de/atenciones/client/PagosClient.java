package com.example.ms_registro.de.atenciones.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_registro.de.atenciones.dto.ApiResponse;
import com.example.ms_registro.de.atenciones.dto.PagosResponse;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class PagosClient {
    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8086/api/pagos/";

    public PagosResponse getPagosClient(Integer id, String token) {

        ApiResponse<PagosResponse> response = webClient.get()
                .uri(BASE_URL + id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<ApiResponse<PagosResponse>>() {})
                .block();

        return response != null ? response.getData() : null;
    }
}
