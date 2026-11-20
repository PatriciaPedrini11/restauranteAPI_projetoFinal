package com.restauranteAPI.restauranteAPI.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "itens_pedido")
@Schema(description = "Item que relaciona um pedido a um prato")
public class ItemPedido {
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Pedido ao qual o item pertence; no envio, informe o ID", example = "{\"id\": 1}")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @Schema(description = "Prato adicionado; no envio, informe o ID", example = "{\"id\": 2}")
    @ManyToOne(optional = false)
    private Prato prato;
    @Schema(description = "Quantidade do prato", example = "2", minimum = "1")
    @Min(1)
    private int quantidade;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Preço copiado automaticamente do prato", example = "32.90", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal precoUnitario;
}

