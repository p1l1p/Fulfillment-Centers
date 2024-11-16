package com.propvuebrand.fulfillmentcenters.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fulfillment Centers API")
                        .version("1.0")
                        .description("REST API для управления продуктами в различных центрах выполнения"));
    }
}