package com.pickpaysimplificado.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    // configurando o RestTemplate
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
