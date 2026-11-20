package com.restauranteAPI.restauranteAPI.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pedidos")
@Schema(description = "Pedido realizado no restaurante")
public class Pedido {
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Data e hora de criação", example = "2026-11-20T19:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime data = LocalDateTime.now();
    @Schema(description = "Situação atual do pedido", example = "ABERTO")
    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.ABERTO;
    @Schema(description = "Valor total calculado automaticamente", example = "65.80", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal valorTotal = BigDecimal.ZERO;
    @Schema(description = "Prato solicitado; no envio, informe o ID", example = "{\"id\": 1}")
    @ManyToOne(optional = false)
    @JoinColumn(name = "prato_id")
    private Prato prato;
    @Schema(description = "Quantidade solicitada", example = "2", minimum = "1")
    @Min(1)
    private int quantidade = 1;

    public enum StatusPedido { ABERTO, PREPARANDO, CONCLUIDO, CANCELADO }
}

