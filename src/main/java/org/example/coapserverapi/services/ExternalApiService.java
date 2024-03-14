package org.example.coapserverapi.services;

import org.example.coapserverapi.models.AccessRequestModel;
import org.example.coapserverapi.models.AccessResponseModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalApiService {
    private final WebClient webClient;

    public ExternalApiService() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:port/ABACMODEL")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public AccessResponseModel sendAccessRequest(AccessRequestModel requestModel) {
        return webClient.put()
                .uri("insertURI")
                .bodyValue(requestModel)
                .retrieve()
                .bodyToMono(AccessResponseModel.class)
                .block();
    }
}
