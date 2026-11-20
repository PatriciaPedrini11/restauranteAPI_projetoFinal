package com.restauranteAPI.restauranteAPI.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurante API",
                version = "1.0",
                description = "API REST para gerenciar o cardápio e os pedidos de um restaurante, " +
                        "com validação, persistência em PostgreSQL e regras de disponibilidade e cálculo de valores.",
                contact = @Contact(
                        name = "Equipe do projeto Restaurante API"
                )
        ),
        tags = {
                @Tag(name = "Categorias", description = "Gerenciamento das categorias do cardápio"),
                @Tag(name = "Pratos", description = "Gerenciamento dos pratos e consulta de disponibilidade"),
                @Tag(name = "Pedidos", description = "Gerenciamento dos pedidos"),
                @Tag(name = "Itens de pedido", description = "Gerenciamento dos itens vinculados a pedidos")
        }
)
public class SwaggerConfig {
}
