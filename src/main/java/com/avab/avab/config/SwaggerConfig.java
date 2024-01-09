package com.avab.avab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI avabAPI() {
        Info info = new Info().title("AvAb API").description("AvAb API 명세").version("0.0.1");

        Components components = new Components();

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info)
                .components(components);
    }
}
