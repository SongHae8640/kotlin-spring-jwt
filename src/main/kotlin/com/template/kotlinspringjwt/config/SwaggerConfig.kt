package com.template.kotlinspringjwt.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI {
        val accessTokenScheme = SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Access JWT Token")

        val refreshTokenScheme = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
            .name("Refresh-Token")
            .description("Refresh JWT Token")

        val components = Components()
            .addSecuritySchemes("Authorization", accessTokenScheme)
            .addSecuritySchemes("Refresh-Token", refreshTokenScheme)

        val securityRequirement = SecurityRequirement()
            .addList("Authorization")
            .addList("Refresh-Token")

        return OpenAPI()
            .components(components)
            .addSecurityItem(securityRequirement)
    }
}