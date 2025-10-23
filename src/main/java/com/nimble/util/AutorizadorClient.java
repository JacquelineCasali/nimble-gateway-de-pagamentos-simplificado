package com.nimble.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AutorizadorClient {

    private static final String URL = "https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer";

    public boolean isAutorizado() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(URL, Map.class);
            return response.getStatusCode().is2xxSuccessful() &&
                    "Autorizado".equalsIgnoreCase((String) response.getBody().get("message"));
        } catch (Exception e) {
            return false;
        }
    }
}

