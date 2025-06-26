package com.upgeekapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração central para a documentação da API via Springdoc OpenAPI 3.
 * Define as informações gerais e o esquema de segurança para a API.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // O nome do esquema de segurança para ser usado na documentação.
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                /*
                 * TODO: A configuração de segurança abaixo deve ser reativada
                 * quando a camada de autenticação (ex: JWT com Auth0) for implementada.
                 * Ela adiciona o botão "Authorize" global e define o esquema Bearer Token.
                 *
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                */
                .info(new Info()
                        .title("UpGeek API")
                        .version("1.0.0")
                        .description("API para o e-commerce de colecionáveis com foco em gamificação UpGeek.")
                        .contact(new Contact()
                                .name("Suporte UpGeek")
                                .email("suporte@upgeek.com")
                                .url("https://www.upgeek.com/contato"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}