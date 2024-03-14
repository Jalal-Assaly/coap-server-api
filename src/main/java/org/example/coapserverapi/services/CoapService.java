package org.example.coapserverapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.coapserverapi.models.AccessRequestModel;
import org.example.coapserverapi.models.AccessResponseModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoapService {

    private final ExternalApiService apiService;

    public void sendAccessRequest(String requestPayload) {

        ObjectMapper jsonToModelMapper = new ObjectMapper();
        AccessRequestModel requestModel;
        try {
            requestModel = jsonToModelMapper.readValue(requestPayload, AccessRequestModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        return apiService.sendAccessRequest(requestModel);
    }
}
