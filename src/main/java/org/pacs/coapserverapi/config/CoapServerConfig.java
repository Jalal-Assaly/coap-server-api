package org.pacs.coapserverapi.config;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.config.CoapConfig;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.elements.config.UdpConfig;
import org.eclipse.californium.elements.util.NetworkInterfacesUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InetSocketAddress;

@Configuration
public class CoapServerConfig {

    @Bean
    public CoapServer coapServer() {
        // Register default CoAP + UDP configuration profiles
        CoapConfig.register();
        UdpConfig.register();

        // Fetch default standards
        org.eclipse.californium.elements.config.Configuration config =
                org.eclipse.californium.elements.config.Configuration.getStandard();

        // Create CoAP server
        CoapServer server = new CoapServer();

        // Scan network and add endpoint
        for (InetAddress addr : NetworkInterfacesUtil.getNetworkInterfaces()) {
            InetSocketAddress bindToAddress = new InetSocketAddress(addr, config.get(CoapConfig.COAP_PORT));
            server.addEndpoint(new CoapEndpoint.Builder().setInetSocketAddress(bindToAddress).build());
        }

        return server;
    }
}