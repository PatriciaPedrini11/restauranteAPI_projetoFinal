package com.restauranteAPI.restauranteAPI.exception;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;
@Schema(description = "Resposta padronizada de erro")
public record ApiError(
        @Schema(example = "2026-11-20T19:30:00") LocalDateTime timestamp,
        @Schema(example = "404") int status,
        @Schema(example = "Recurso não encontrado") String erro,
        @Schema(example = "Prato com ID 99 não encontrado") String mensagem,
        Map<String, String> campos) {}
