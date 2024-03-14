package org.example.coapserverapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;

@SpringBootApplication(exclude = {WebMvcAutoConfiguration.class})
public class CoapServerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoapServerApiApplication.class, args);
    }

}
