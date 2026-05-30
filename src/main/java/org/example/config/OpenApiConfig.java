package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jewelryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Jewelry Shop API")
                        .description("REST API для магазина ювелирных украшений")
                        .version("1.0.0")
                );
    }
}
