package com.nimble.util;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AutorizadorCartaoClient {

    private final RestTemplate restTemplate;

    public AutorizadorCartaoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isCartaoAutorizado(Map<String, Object> dadosCartao) {
        try {
            // URL do autorizador de cartão (exemplo)
            String url = "https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer/cartao";

            Map response = restTemplate.postForObject(url, dadosCartao, Map.class);
            return response != null && Boolean.TRUE.equals(response.get("autorizado"));
        } catch (Exception e) {
            return false;
        }
    }
}
