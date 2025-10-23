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
            if (!response.getStatusCode().is2xxSuccessful()) {
                return false;
            }
            Map<String, Object> body = response.getBody();
            if (body == null) return false;

            Object dataObj = body.get("data");
            if (!(dataObj instanceof Map)) return false;

            Map<String, Object> data = (Map<String, Object>) dataObj;
            Object authorizedObj = data.get("authorized");
            if (!(authorizedObj instanceof Boolean)) return false;
                      return (Boolean) authorizedObj;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

