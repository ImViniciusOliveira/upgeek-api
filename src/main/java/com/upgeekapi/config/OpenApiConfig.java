package com.upgeekapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // O nome do esquema de segurança foi mantido para quando for reativado.
        //final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                // Define as informações gerais da sua API (título, descrição, etc.)
                .info(new Info()
                        .title("UpGeek API")
                        .version("1.0.0")
                        .description("API para o e-commerce de colecionáveis com foco em gamificação UpGeek.")
                );

        /*
         * O CÓDIGO ABAIXO FOI COMENTADO E DEVE SER REATIVADO QUANDO A SEGURANÇA FOR IMPLEMENTADA.
         * Ele é responsável por adicionar o botão "Authorize" e definir como o token JWT funciona.
         */

        /*
                // Adiciona o botão "Authorize" global na UI do Swagger
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // Define COMO a segurança "bearerAuth" funciona
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                );
        */
    }
}
