package com.adrianbcodes.currencyexchange.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI currencyExchangeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Currency Exchange API")
                        .description("Microservice that serves currency exchange data")
                        .version("1.0.0"));
    }
}
