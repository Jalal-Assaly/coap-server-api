package org.pacs.coapserverapi.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.pacs.coapserverapi.models.AccessResponseModel;
import org.pacs.coapserverapi.services.CoapService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class CoapAccessController extends CoapServer {

    private final CoapServer coapServer;
    private final CoapService coapService;

    @PostConstruct
    public void init() {
        coapServer.add(new AccessControlResource(coapService));
        // Start the server
        coapServer.start();
    }

    private static class AccessControlResource extends CoapResource {
        private final CoapService coapService;
        public AccessControlResource(CoapService coapService) {
            // Define URI
            super("accessControl");
            // Declare coapService inside class scope
            this.coapService = coapService;
        }

        @Override
        public void handleGET(CoapExchange exchange) {
            Request request = exchange.advanced().getRequest();
            System.out.println(Utils.prettyPrint(request));
            exchange.respond(CoAP.ResponseCode.CONTENT, "Well received !", 0);
        }

        @Override
        public void handlePUT(CoapExchange exchange) {

            // Endpoint to be called
            String endpoint = "employee";

            // Received request
            Request request = exchange.advanced().getRequest();
            System.out.println(Utils.prettyPrint(request));
            System.out.println(request.getPayloadString());

            // Visitor or employee role
            byte[] payload = request.getPayload();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map<String, Map<String, Object>> actualJson = objectMapper.readValue(payload, new TypeReference<>(){});
                if (actualJson.get("userAttributes").get("role").equals("Visitor")) {
                    endpoint = "visitor";
                }
            } catch (IOException e) {
                throw new RuntimeException("JSON deserialization exception");
            }

            // Communicate with ABAC Model
            AccessResponseModel response;
            try {
                response = coapService.sendAccessRequest(endpoint, request.getPayloadString());
            } catch (WebClientResponseException exception) {
                response = new AccessResponseModel(false);
            }

            // Send response
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String jsonResponse;
            try {
                jsonResponse = objectWriter.writeValueAsString(response);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            exchange.respond(CoAP.ResponseCode.CONTENT, jsonResponse, 50);
        }
    }
}


