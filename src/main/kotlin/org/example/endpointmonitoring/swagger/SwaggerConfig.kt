package org.example.endpointmonitoring.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
//        nastavenie info ohladom open API
            .info(
                Info().title("API Documentation")
                    .version("1.0.0")
                    .description("Your API description")
            )
//            nastavenie autorizacie pomocou headru. Cusotom nazov headru. Vieme pouzit aj uz vytvorene security schemy...napr. pre JWT
            .addSecurityItem(SecurityRequirement().addList("accessToken"))
            .components(
                Components()
                    .addSecuritySchemes(
                        "accessToken",
                        SecurityScheme().type(SecurityScheme.Type.APIKEY)
                            .`in`(SecurityScheme.In.HEADER)
                            .name("accessToken")
                    )
            )
    }
}