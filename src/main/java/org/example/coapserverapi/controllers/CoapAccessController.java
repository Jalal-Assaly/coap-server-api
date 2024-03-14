package org.example.coapserverapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.example.coapserverapi.services.CoapService;
import org.springframework.stereotype.Component;


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
        public void handlePUT(CoapExchange exchange) {

            // Received request
            Request request = exchange.advanced().getRequest();
            System.out.println(Utils.prettyPrint(request));

            // Communicate with ABAC Model
            coapService.sendAccessRequest(request.getPayloadString());

            // Send response
            exchange.respond(CoAP.ResponseCode.CONTENT, "OK");
        }
    }
}


