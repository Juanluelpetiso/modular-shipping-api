package com.jretamales.shipping.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI / Swagger UI.
 * Accesible en: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI shippingOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Modular Shipping API")
                .description(
                    "API REST modular para cálculo de tarifas de envío. " +
                    "Implementada con Clean Architecture y el patrón Strategy " +
                    "para desacoplar los módulos de cálculo (Nacional, Internacional, Express)."
                )
                .version("1.0.0")
                .contact(new Contact()
                    .name("J. Retamales")
                )
            );
    }
}
