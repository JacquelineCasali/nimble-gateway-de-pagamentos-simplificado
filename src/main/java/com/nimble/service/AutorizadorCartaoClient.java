//package com.nimble.service;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Service
//public class AutorizadorCartaoClient {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    private static final String URL_CARTAO = "https://zsy6tx7aql.execute-api.sa-east-1.amazonaws.com/authorizer";
//
//    public boolean autorizarPagamentoCartao(PagamentoCartaoDto pagamento) {
//        try {
//            ResponseEntity<Map> response = restTemplate.postForEntity(URL_CARTAO, pagamento, Map.class);
//            if (!response.getStatusCode().is2xxSuccessful()) return false;
//
//            Map<String, Object> body = response.getBody();
//            if (body == null) return false;
//
//            Object dataObj = body.get("data");
//            if (!(dataObj instanceof Map)) return false;
//
//            Map<String, Object> data = (Map<String, Object>) dataObj;
//            Object authorizedObj = data.get("authorized");
//            return authorizedObj instanceof Boolean && (Boolean) authorizedObj;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
