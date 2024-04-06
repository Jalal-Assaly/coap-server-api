package org.pacs.coapserverapi.services;

import lombok.RequiredArgsConstructor;
import org.pacs.coapserverapi.models.AccessResponseModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoapService {

    private final ExternalApiService apiService;

    public AccessResponseModel sendAccessRequest(String endpoint, String requestPayload) {
        return apiService.sendAccessRequest(endpoint, requestPayload);
    }
}
