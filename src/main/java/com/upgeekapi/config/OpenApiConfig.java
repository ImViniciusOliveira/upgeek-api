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
 * Configura a documentação da API gerada pelo Springdoc, baseada na especificação OpenAPI 3.
 * <p>
 * Esta classe é responsável por definir metadados essenciais da API, como título,
 * versão e informações de contato, além de configurar o esquema de segurança JWT.
 * Isso permite que a interface do Swagger UI exiba um botão 'Authorize', facilitando
 * o teste de endpoints protegidos diretamente pelo navegador.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    /**
     * Cria e personaliza o bean {@link OpenAPI} que define a estrutura da documentação.
     * <p>
     * A configuração é feita em três partes principais:
     * <ol>
     *     <li>Definição do esquema de segurança JWT.</li>
     *     <li>Aplicação global deste esquema a todos os endpoints.</li>
     *     <li>Definição das informações gerais da API (título, versão, etc.).</li>
     * </ol>
     *
     * @return Um objeto {@link OpenAPI} totalmente configurado para a aplicação.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Adiciona o requisito de segurança global, fazendo com que o cadeado apareça em todos os endpoints.
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                // Define os componentes reutilizáveis, como os esquemas de segurança.
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, createBearerAuthScheme())
                )
                // Define as informações gerais da API exibidas no topo da página do Swagger.
                .info(createApiInfo());
    }

    /**
     * Cria o objeto de informações da API.
     *
     * @return Um objeto {@link Info} com os metadados da API.
     */
    private Info createApiInfo() {
        return new Info()
                .title("UpGeek API")
                .version("1.0.0")
                .description("API para o e-commerce de colecionáveis com foco em gamificação UpGeek.")
                .contact(new Contact()
                        .name("Suporte UpGeek")
                        .email("suporte@upgeek.com")
                        .url("https://www.upgeek.com/contato"))
                .license(new License()
                        .name("GPL-3.0 license")
                        .url("https://opensource.org/license/GPL-3.0"));
    }

    /**
     * Cria a definição do esquema de segurança para autenticação JWT (Bearer Token).
     *
     * @return Um objeto {@link SecurityScheme} configurado para JWT.
     */
    private SecurityScheme createBearerAuthScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}