package org.example.coapserverapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccessResponseModel {
    private Boolean decision;
    private String reason;
}
