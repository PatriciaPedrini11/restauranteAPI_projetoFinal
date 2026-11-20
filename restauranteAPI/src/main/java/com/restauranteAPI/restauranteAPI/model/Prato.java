package com.restauranteAPI.restauranteAPI.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "pratos")
@Schema(description = "Prato oferecido no cardápio do restaurante")
public class Prato {
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nome do prato", example = "Lasanha à bolonhesa")
    @NotBlank
    @Size(min = 2, max = 100)
    private String nome;
    @Schema(description = "Descrição do prato", example = "Lasanha com molho de carne e queijo")
    @Size(max = 255)
    private String descricao;
    @Schema(description = "Preço atual do prato", example = "32.90")
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal preco;
    @Schema(description = "Indica se o prato pode ser incluído em pedidos", example = "true")
    private boolean disponivel = true;
    @Schema(description = "Categoria do prato; no envio, informe ao menos o ID")
    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}

