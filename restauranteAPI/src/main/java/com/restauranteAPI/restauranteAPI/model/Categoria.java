package com.restauranteAPI.restauranteAPI.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.*;

@Data
@Entity
@Table(name = "categorias")
@Schema(description = "Categoria usada para organizar os pratos do cardápio")
public class Categoria {
    @Schema(description = "Identificador único", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nome da categoria", example = "Massas")
    @NotBlank
    @Size(min = 2, max = 60)
    private String nome;
    @Schema(description = "Descrição opcional da categoria", example = "Pratos à base de massa")
    @Size(max = 255)
    private String descricao;
    @JsonIgnore
    @OneToMany(mappedBy = "categoria")
    private List<Prato> pratos = new ArrayList<>();
}


