package com.pickpaysimplificado.sprigdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configuration classe de configuração da documentacao
@Configuration
public class SpringdocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                //@OpenAPIDefinition(
                .info(new Info().title("PicPay Simplificado")
                        .description("Api responsável por cadastro e transferência")
                        .version("1")
                )


                .components(new Components());

    }

}
